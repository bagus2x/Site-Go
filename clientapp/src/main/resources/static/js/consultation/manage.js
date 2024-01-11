// Setup Data Table
$(function () {
    const dataTable = $("#table-consultation").DataTable({
        columnDefs: [{width: '5%', targets: 0}],
        ajax: {
            method: 'GET',
            url: '/api/consultations',
            dataSrc: '',
        },
        responsive: true,
        'fnInitComplete': function () {
            new PerfectScrollbar('#table-consultation_wrapper', {
                useBothWheelAxes: false
            });

            $('#select-status')
                .val('WAITING')
                .trigger('change')
        },
        'fnDrawCallback': function () {
            new PerfectScrollbar('#table-consultation_wrapper', {
                useBothWheelAxes: false
            });
            $('.dataTables_length select').select2({
                dropdownParent: $('.card-header'),
                minimumResultsForSearch: Infinity
            })
        },
        columns: [
            {
                data: null,
                render: (_data, _row, _type, meta) => {
                    return meta.row + 1;
                },
            },
            {
                data: null,
                render: (consultation) => {
                    return `
                         <a class="d-flex" href="/profile/${consultation.counselee.id}">
                          <div class="d-flex align-items-center">
                            <img src="${consultation.counselee.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed=${consultation.counselee.name}`}" class="avatar avatar-sm rounded-circle me-2" alt="user1">
                          </div>
                          <div class="d-flex flex-column justify-content-center ms-1 text-truncate">
                            <h6 class="mb-0 text-sm font-weight-semibold text-truncate" style="font-size: 0.875rem">${consultation.counselee.name}</h6>
                            <p class="text-sm text-secondary mb-0 text-truncate" style="font-size: 0.875rem">${consultation.counselee.user.email}</p>
                          </div>
                        </a>
                    `
                }
            },
            {
                data: null,
                render: (consultation) => {
                    if (!consultation.consultant) return `-`

                    return `
                         <a class="d-flex" href="/profile/${consultation.consultant.id}">
                          <div class="d-flex align-items-center">
                            <img src="${consultation.consultant.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed=${consultation.consultant.name}`}" class="avatar avatar-sm rounded-circle me-2" alt="user1">
                          </div>
                          <div class="d-flex flex-column justify-content-center ms-1 text-truncate">
                            <h6 class="mb-0 text-sm font-weight-semibold text-truncate" style="font-size: 0.875rem">${consultation.consultant.name}</h6>
                            <p class="text-sm text-secondary mb-0 text-truncate" style="font-size: 0.875rem">${consultation.consultant.user.email}</p>
                          </div>
                        </a>
                    `
                }
            },
            {
                data: 'status',
                render: status
            },
            {
                data: null,
                render: time
            },
            {
                data: 'createdAt',
                render: (createdAt) => {
                    return `<span>${dayjs(createdAt).format('DD MMM YYYY HH:mm')}</span>`
                }
            },
            {
                data: null,
                render: (consultation) => {
                    const status = consultation.status
                    const canValidate = status.name === 'WAITING' && hasRoleAdmin
                    const canReject = status.name === 'WAITING' && hasRoleAdmin
                    const canSchedule = status.name === 'VALIDATED' && hasRoleAdmin
                    const canWriteReport = status.name === 'SCHEDULED' && (hasRoleConsultant && consultation.consultant?.id === profile.id || hasRoleAdmin)

                    return `
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                              <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                            <div class="dropdown-menu zindex-5">
                              <a 
                                class="dropdown-item" 
                                href="javascript:void(0);"
                                data-bs-toggle="modal" 
                                data-bs-target="#modal-detail"
                                onclick="showDetail(${consultation.id})"
                              >
                                <i class="bx bx-detail me-1"></i> Detail
                              </a >
                              <a 
                                  class="dropdown-item ${!canValidate ? 'disabled' : ''}" 
                                  href="javascript:void(0);"
                                  onclick="validate(${consultation.id}, '${consultation.counselee.name}')"
                               >
                                <i class="bx bx-check me-1"></i> Validate
                              </a >
                              <a
                                class="dropdown-item ${!canReject ? 'disabled' : ''}" 
                                href="javascript:void(0);"
                                data-bs-toggle="modal" 
                                data-bs-target="#modal-rejection" 
                                onclick="reject(${consultation.id}, '${consultation.counselee.name}')"
                              >
                                <i class="bx bx-x me-1"></i> Reject
                              </a>
                              <a 
                                class="dropdown-item ${!canSchedule ? 'disabled' : ''}" 
                                href="javascript:void(0);"
                                data-bs-toggle="modal" 
                                data-bs-target="#modal-schedule" 
                                onclick="schedule(${consultation.id}, '${consultation.counselee.name}')"
                              >
                                <i class="bx bx-calendar-edit me-1"></i> Schedule
                              </a>
                              <a 
                                class="dropdown-item ${!canWriteReport ? 'disabled' : ''}" 
                                href="javascript:void(0);"
                                data-bs-toggle="modal" 
                                data-bs-target="#modal-write-report"
                                onclick="finish(${consultation.id}, '${consultation.counselee.name}')"
                              >
                                <i class="bx bx-check-double me-1"></i> Write report & mark as done
                              </a>
                            </div>
                        </div>
                    `
                },
            },
        ]
    })

    const dtButtons = new $.fn.dataTable.Buttons(dataTable, {
        buttons: [
            'copyHtml5',
            'excelHtml5',
            'csvHtml5',
            'pdfHtml5'
        ]

    }).container();

    const icons = [
        $(`<i class='bx bx-copy-alt me-1' ></i>`),
        $(`<i class='bx bx-spreadsheet me-1' ></i>`),
        $(`<i class='bx bxs-file-doc me-1' ></i>`),
        $(`<i class='bx bxs-file-pdf me-1' ></i>`)
    ]

    dtButtons.children().each(function (index, element) {
        $(element)
            .prepend(icons[index])
            .addClass('dropdown-item d-flex align-items-center')
            .wrap('<li></li>')
            .appendTo('#dropdown-export .dropdown-menu')
    })

    $('#select-status').on('change', function () {
        const value = $('#select-status').val()

        if (!value) {
            dataTable.column(3).search('').draw()
        } else {
            dataTable.column(3).search(value).draw()
        }
    })
})

$(function () {
    $('#input-schedule-time').flatpickr({
        minDate: 'today',
        enableTime: true,
        minTime: "08:00",
        maxTime: "17:00"
    })
})

$(function () {
    const fullToolbar = [
        [
            {
                font: []
            },
            {
                size: []
            }
        ],
        ['bold', 'italic', 'underline', 'strike'],
        [
            {
                color: []
            },
            {
                background: []
            }
        ],
        [
            {
                script: 'super'
            },
            {
                script: 'sub'
            }
        ],
        [
            {
                header: '1'
            },
            {
                header: '2'
            },
            'blockquote',
            'code-block'
        ],
        [
            {
                list: 'ordered'
            },
            {
                list: 'bullet'
            },
            {
                indent: '-1'
            },
            {
                indent: '+1'
            }
        ],
        [
            'direction',
            {
                align: []
            }
        ],
        ['link', 'image', 'video', 'formula'],
        ['clean']
    ];

    new Quill('#full-editor', {
        bounds: '#full-editor',
        placeholder: 'Type Something...',
        modules: {
            formula: true,
            toolbar: fullToolbar
        },
        theme: 'snow'
    });
})

function validate(id, name) {
    Swal.fire({
        title: `Do you want to validate request from ${name}?`,
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: "Save",
        denyButtonText: `Don't save`
    }).then((result) => {
        if (!result.isConfirmed) return

        $.ajax({
            method: 'PATCH',
            url: `/api/consultation/${id}/validate`,
            dataType: 'JSON',
            contentType: 'application/json',
            beforeSend: addCsrfHeader,
            success: () => {
                $('#table-consultation').DataTable().ajax.reload();
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Request successfully validated...',
                    showConfirmButton: false,
                    timer: 2000,
                });
            },
            error: (err) => {
                console.log(err);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                });
            },
        });
    });
}

function reject(id, name) {
    $('#title-rejection').text(`Reject Request From ${name}`)
    $('#input-rejection-description').val('')

    $('#btn-reject').off().on('click', function () {
        const description = $('#input-rejection-description').val()

        if (!description) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please choose the description',
            });
            return
        }

        $('#modal-rejection').block({
            message: '<div class="spinner-border text-primary" role="status"></div>',
            css: {
                backgroundColor: 'transparent',
                border: '0'
            },
            overlayCSS: {
                backgroundColor: '#000',
                opacity: 0.25
            }
        })

        $.ajax({
            method: 'PATCH',
            url: `/api/consultation/${id}/reject`,
            dataType: 'JSON',
            contentType: 'application/json',
            data: JSON.stringify({description}),
            beforeSend: addCsrfHeader,
            success: () => {
                console.log('success');
                $('#table-consultation').DataTable().ajax.reload();
                $('#modal-rejection').unblock().modal('hide')

                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Request successfully rejected...',
                    showConfirmButton: false,
                    timer: 2000,
                });
            },
            error: (err) => {
                console.log(err);
                $('#modal-rejection').unblock()

                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                });
            },
        });
    })
}

function schedule(id, name) {
    $('#title-schedule').text(`Schedule Consultation From ${name}`)

    $.ajax({
        method: 'GET',
        url: `/api/profiles?roles='CONSULTANT'`,
        dataType: 'JSON',
        contentType: 'application/json',
        success: (consultants) => {
            $('#select-schedule-consultant').empty()
            consultants.forEach((consultant) => {
                const option = `
                  <option 
                    data-photo="${consultant.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed=${consultant.name}`}" 
                    value="${consultant.id}">
                      ${consultant.name}
                    </option>
                `
                $('#select-schedule-consultant').append(option)
            })
        },
        error: (err) => {
            console.log(err);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Failed to load consultant',
            });
        },
    })

    $('#btn-schedule').off().on('click', function () {
        const consultantId = $('#select-schedule-consultant').val()
        const venue = $('#input-schedule-venue').val()
        const time = $('#input-schedule-time').val()
        const duration = $('#input-schedule-duration').val()
        const description = $('#input-schedule-description').val()

        if (!consultantId) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please choose consultant',
            });
            return;
        }

        if (!venue) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please choose the venue',
            });
            return;
        }

        if (!time) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please choose the time',
            });
            return;
        }

        if (!duration) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please enter the duration',
            });
            return;
        }

        if (!description) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please enter the description',
            });
            return
        }

        const durationInMinutes = parseInt(duration)
        const timeInEpoch = new Date(time).getTime()

        $('#modal-schedule').block({
            message: '<div class="spinner-border text-primary" role="status"></div>',
            css: {
                backgroundColor: 'transparent',
                border: '0'
            },
            overlayCSS: {
                backgroundColor: '#000',
                opacity: 0.25
            }
        })

        $.ajax({
            method: 'POST',
            url: '/api/consultation/check-conflict-schedule',
            beforeSend: addCsrfHeader,
            dataType: 'JSON',
            contentType: 'application/json',
            data: JSON.stringify({
                consultantId,
                time: timeInEpoch,
                duration: durationInMinutes,
            }),
            success: (hasConflict) => {
                // Warn admin selected if a consultant has a conflict schedule
                // TODO: make the code cleaner
                if (hasConflict) {
                    Swal.fire({
                        title: "Do you want to schedule this request",
                        text: 'This consultant has conflict schedule',
                        showDenyButton: true,
                        showCancelButton: true,
                        confirmButtonText: "Schedule",
                        denyButtonText: `Don't save`
                    }).then((result) => {
                        if (result.isConfirmed) {
                            $.ajax({
                                method: 'PATCH',
                                url: `/api/consultation/${id}/schedule`,
                                dataType: 'JSON',
                                contentType: 'application/json',
                                data: JSON.stringify({
                                    consultantId,
                                    venue,
                                    time: timeInEpoch,
                                    duration: durationInMinutes,
                                    description
                                }),
                                beforeSend: addCsrfHeader,
                                success: () => {
                                    $('#modal-schedule').unblock().modal('hide')
                                    $('#table-consultation').DataTable().ajax.reload()

                                    Swal.fire({
                                        position: 'center',
                                        icon: 'success',
                                        title: 'Request successfully scheduled 🚀...',
                                        showConfirmButton: false,
                                        timer: 2000,
                                    });
                                },
                                error: (err) => {
                                    console.log(err);
                                    $('#modal-schedule').unblock()

                                    Swal.fire({
                                        icon: 'error',
                                        title: 'Oops...',
                                        text: 'Something went wrong!',
                                    });
                                },
                            });
                        } else {
                            $('#modal-schedule').unblock()
                        }
                    });
                    return
                }

                $.ajax({
                    method: 'PATCH',
                    url: `/api/consultation/${id}/schedule`,
                    dataType: 'JSON',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        consultantId,
                        venue,
                        time: timeInEpoch,
                        duration: durationInMinutes,
                        description
                    }),
                    beforeSend: addCsrfHeader,
                    success: () => {
                        $('#modal-schedule').unblock().modal('hide')
                        $('#table-consultation').DataTable().ajax.reload()

                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Request successfully scheduled...',
                            showConfirmButton: false,
                            timer: 2000,
                        });
                    },
                    error: (err) => {
                        console.log(err);
                        $('#modal-schedule').unblock()

                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Something went wrong!',
                        });
                    },
                });
            },
            error: (err) => {
                console.log(err);
                $('#modal-schedule').unblock()

                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                });
            },
        })
    })
}

function finish(id, name) {
    $('#title-write-report').text(`Write Consultation Report For ${name}`)

    $('#btn-save-report').off().on('click', function () {
        const description = $('.ql-editor').html()

        if (!description) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please write the description',
            });
            return
        }

        $('#modal-write-report').block({
            message: '<div class="spinner-border text-primary" role="status"></div>',
            css: {
                backgroundColor: 'transparent',
                border: '0'
            },
            overlayCSS: {
                backgroundColor: '#000',
                opacity: 0.25
            }
        })

        $.ajax({
            method: 'PATCH',
            url: `/api/consultation/${id}/finish`,
            dataType: 'JSON',
            contentType: 'application/json',
            data: JSON.stringify({description}),
            beforeSend: addCsrfHeader,
            success: () => {
                $('#table-consultation').DataTable().ajax.reload();
                $('#modal-write-report').unblock().modal('hide')

                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Request successfully marked as done...',
                    showConfirmButton: false,
                    timer: 2000,
                });
            },
            error: (err) => {
                console.log(err);
                $('#modal-write-report').unblock()

                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                });
            },
        });
    })
}

// filter status
$(function () {
    $('#filter-status').appendTo('#table-consultation_filter')
    $('#filter-status .select2').select2({
        dropdownParent: $('.card-header'),
        templateResult: consultationStatusTemplate,
        templateSelection: consultationStatusTemplate,
    })

    function consultationStatusTemplate(option) {
        const val = $(option.element).val()
        const text = $(option.element).text()

        let color = 'text-secondary'
        switch (val) {
            case 'WAITING':
                color = 'text-warning'
                break
            case 'VALIDATED':
                color = 'text-primary'
                break
            case 'REJECTED':
                color = 'text-danger'
                break
            case 'SCHEDULED':
                color = 'text-info'
                break
            case 'DONE':
                color = 'text-success'
                break
        }

        return $(`<span class="d-flex gap-1 align-items-center"><i class="bx bxs-circle bx-xs me-2 ${color}"></i> <span>${text}</span></span>`);
    }

    $('#modal-schedule .select2').select2({
        dropdownParent: $('#modal-schedule'),
        templateSelection: consultantTemplate(),
        templateResult: consultantTemplate('style="font-size: 13px;"')
    })

    function consultantTemplate(style) {
        return (option) => {
            const photo = $(option.element).data('photo')
            const name = $(option.element).text()
            if (!photo || !name) return

            return $(`
                <div class="d-flex gap-1 align-items-center" ${style}>
                    <img 
                      src="${photo}" 
                      class="avatar avatar-xs rounded-circle me-2" 
                      alt="consultant"
                      style="width: 1rem; height: 1rem"
                    >
                    <span>${name}</span>
                </div>`
            )
        }
    }
});
