// Setup Data Table
$(function () {
    $("#table-employee").DataTable({
        columnDefs: [{width: '5%', targets: 0}],
        ajax: {
            method: "GET",
            url: "/api/profiles",
            dataSrc: "",
        },
        responsive: true,
        "fnInitComplete": function () {
            new PerfectScrollbar('#table-employee_wrapper');

            $('.dataTables_length select').select2({
                dropdownParent: $('.card-header'),
                minimumResultsForSearch: Infinity
            })
        },
        "fnDrawCallback": function () {
            new PerfectScrollbar('#table-employee_wrapper');
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
                render: (profile) => {
                    return `
                         <a class="d-flex" href="/profile/${profile.id}">
                          <div class="d-flex align-items-center">
                            <img src="${profile.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed==${profile.name}`}" class="avatar avatar-sm rounded-circle me-2" alt="user1">
                          </div>
                          <div class="d-flex flex-column justify-content-center ms-1">
                            <h6 class="mb-0 text-sm font-weight-semibold" style="font-size: 0.875rem">${profile.name}</h6>
                            <p class="text-sm text-secondary mb-0" style="font-size: 0.875rem">${profile.user.email}</p>
                          </div>
                        </a>
                    `
                }
            },
            {
                data: 'user.roles',
                render: (roles) => {
                    const roleNames = roles.map((role) => role.name.toLowerCase()).join(', ')
                    return `<span class="text-end text-secondary text-sm font-weight-normal text-capitalize">${roleNames}</span>`
                }
            },
            {
                data: 'user.isEnabled',
                render: (isEnabled) => {
                    if (isEnabled) {
                        return `<span class="badge bg-label-success me-1">Verified</span>`
                    }
                    return `<span class="badge bg-label-danger me-1">Not Verified</span>`
                }
            },
            {
                data: 'status',
                render: (status) => {
                    if (status && status.name === 'ONSITE') {
                        return `<span class="badge bg-label-success me-1">On Site</span>`
                    }
                    return `<span class="badge bg-label-danger me-1">Idle</span>`
                }
            },
            {
                data: null,
                render: (profile) => {
                    const canUpdateProfile = hasRoleAdmin

                    return `
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                              <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                            <div class="dropdown-menu">
                              <a 
                                  class="dropdown-item ${!canUpdateProfile ? 'disabled' : ''}" 
                                  href="javascript:void(0);"
                                  data-bs-toggle="modal"
                                  data-bs-target="#modal-update-employee"
                                  onclick="update(${profile.id})"
                              >
                                <i class="bx bx-edit-alt me-1"></i> Update</a
                              >
                              <a class="dropdown-item disabled" href="javascript:void(0);"
                                ><i class="bx bx-trash me-1"></i> Delete</a
                              >
                            </div>
                        </div>
                    `
                },
            },
        ]
    })
})

$(function () {
    $('#btn-add').on('click', function () {
        const name = $('#input-name').val()
        const email = $('#input-email').val()

        if (!name) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please enter the name!",
            });
            return
        }

        if (!email) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please enter the name!",
            });
            return
        }

        $('#modal-register-employee').block({
            message: '<div class="spinner-border text-primary" role="status"></div>',
            css: {
                backgroundColor: 'transparent',
                border: '0'
            },
            overlayCSS: {
                backgroundColor: '#fff',
                opacity: 0.8
            }
        })

        $.ajax({
            method: 'POST',
            url: '/api/registration',
            dataType: 'JSON',
            contentType: 'application/json',
            data: JSON.stringify({name, email}),
            beforeSend: addCsrfHeader,
            success: function () {
                $("#table-employee").DataTable().ajax.reload()
                $('#input-name').val('')
                $('#input-email').val('')

                $('#modal-register-employee').unblock().modal('hide')
            },
            error: (err) => {
                console.log(err);
                $('#modal-register-employee').unblock()

                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Something went wrong!",
                });
            }
        })
    })
})

function update(profileId) {
    $.ajax({
        method: 'GET',
        url: `/api/profile-statuses`,
        success: (statuses) => {
            $('#select-profile-status').empty()
            statuses.forEach((status) => {
                $('#select-profile-status').append(`<option class="text-capitalize" value="${status.name}">${status.name.toLowerCase()}</option>`)
            })

            $.ajax({
                method: 'GET',
                url: `/api/profile/${profileId}`,
                success: (profile) => {
                    $('#input-profile-name').val(profile.name)
                    $('#input-select-gender').val(profile.gender)
                    $('#input-profile-phone').val(profile.phone)
                    $('#preview-cv').attr('href', profile.cv)

                    $('#cb-roles input').each((_, element) => {
                        $(element).prop('checked', profile.user.roles.some((role) => role.name === $(element).val()))
                    })
                },
                error: (err) => {
                    console.log(err)
                }
            })
        },
        error: (err) => {
            console.log(err)
        }
    })

    $('#btn-update').off().on('click', function () {
        const name = $('#input-profile-name').val()
        const gender = $('#select-profile-gender').val()
        const phone = $('#input-profile-phone').val()
        const cv = $('#input-profile-cv').prop('files')[0]
        const status = $('#select-profile-status').val()
        const roles = $('#cb-roles input[type=checkbox]:checked')
            .serializeArray()
            .map((role) => role.value)

        if (!name) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please enter the name!",
            });
            return
        }

        if (!gender) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please choose the gender",
            });
            return
        }

        if (!phone) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please enter the phone number",
            });
            return
        }

        if (!status) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please choose the profile status",
            });
            return
        }

        if (!roles.length) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please select roles",
            });
            return
        }

        const formData = new FormData()
        formData.append('name', name)
        formData.append('gender', gender)
        formData.append('phone', phone)
        if (cv) formData.append('cv', cv)
        formData.append('status', status)
        formData.append('roles', roles)

        $('#modal-update-employee').block({
            message: '<div class="spinner-border text-primary" role="status"></div>',
            css: {
                backgroundColor: 'transparent',
                border: '0'
            },
            overlayCSS: {
                backgroundColor: '#fff',
                opacity: 0.8
            }
        })

        $.ajax({
            method: 'PUT',
            url: `/api/profile/${profileId}`,
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: addCsrfHeader,
            success: () => {
                $("#table-employee").DataTable().ajax.reload()
                $('#modal-update-employee').unblock().modal('hide')

                Swal.fire({
                    title: `Successfully update data`,
                    icon: 'success'
                });
            },
            error: (e) => {
                console.log(e)
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Something went wrong!",
                });
            }
        })
    })
}
