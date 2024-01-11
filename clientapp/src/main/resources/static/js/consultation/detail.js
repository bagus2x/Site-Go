function showDetail(id) {
    $.ajax({
        method: 'GET',
        url: `/api/consultation/${id}`,
        dataType: 'JSON',
        contentType: 'application/json',
        success: (consultation) => {
            $('#td-counselee').empty().append(`
              <div class="d-flex gap-3 align-items-center">
                  <div
                    data-toggle="tooltip" 
                    data-placement="bottom"
                    class="tooltip-avatar avatar avatar-xs pull-up"
                    title="Sophia Wilkerson"
                  >
                    <img src="${consultation.counselee.photo || `https://api.dicebear.com/7.x/fun-emoji/svg?seed=${consultation.counselee.name}`}" alt="Avatar" class="rounded-circle" />
                  </div>
                  <span>${consultation.counselee.name}</span>
              </div>
            `)

            if (consultation.consultant) {
                $('#td-consultant').empty().append(`
                  <div class="d-flex gap-3 align-items-center">
                      <div
                        data-toggle="tooltip" 
                        data-placement="bottom"
                        class="tooltip-avatar avatar avatar-xs pull-up"
                        title="Sophia Wilkerson"
                      >
                        <img src="${consultation.consultant.photo || `https://api.dicebear.com/7.x/fun-emoji/svg?seed=${consultation.consultant.name}`}" alt="Avatar" class="rounded-circle" />
                      </div>
                      <span>${consultation.consultant.name}</span>
                  </div>
                `)
            }

            $('#td-status').empty().append(status(consultation.status))
            $('#td-time').empty().append(time(consultation))
            $('#td-requested-at').empty().append(`<span>${dayjs(consultation.createdAt).format('DD MMM YYYY HH:mm')}</span>`)
            $('#td-venue').empty().append(`<span>${consultation.venue || '-'}</span>`)

            $('#timeline-container').empty()

            consultation.histories.reverse().forEach((history, index) => {
                $('#timeline-container').append(`
                    <div class="tl-item ${index === 0 ? 'active' : ''}">
                        <div class="tl-dot b-primary"></div>
                        <div class="tl-content">
                            <div class="d-flex gap-1 align-items-center">
                              ${status(history.id.consultationStatus)}
                              <span class="text-muted text-sm">by ${history.editor.name}</span>
                            </div>
                            ${history.description ? `<div class="tl-info text-muted mt-1">${history.description}</div>` : ''}
                            <div class="tl-info text-muted mt-1">${dayjs(history.createdAt).format('DD MMM YYYY HH:mm')}</div>
                        </div>
                    </div>
                `)
            })

            const btnShowReport = $('#btn-report')
            btnShowReport.addClass('d-none')
            if (consultation.status.name === 'DONE') {
                btnShowReport.removeClass('d-none')

                function handleClick() {
                    showReport(id)
                }

                btnShowReport.off().on('click', handleClick)

                $('#modal-report').on('hide.bs.modal', function () {
                    btnShowReport.off('click', handleClick)
                })
            }

            $(".tooltip-avatar").tooltip();
        },
        error: (err) => {
            console.log(err);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Failed to load consultation',
            });
        },
    })
}

function status(status) {
    if (status.name === 'WAITING') {
        return `<span class="badge bg-label-warning me-1">Waiting</span>`
    }
    if (status.name === 'VALIDATED') {
        return `<span class="badge bg-label-primary me-1">Validated</span>`
    }
    if (status.name === 'REJECTED') {
        return `<span class="badge bg-label-danger me-1">Rejected</span>`
    }
    if (status.name === 'SCHEDULED') {
        return `<span class="badge bg-label-info me-1">Scheduled</span>`
    }
    if (status.name === 'DONE') {
        return `<span class="badge bg-label-success me-1">Done</span>`
    }
    return `<span class="badge bg-label-danger me-1">Not Verified</span>`
}

function time(consultation) {
    if (!consultation.time || !consultation.duration) {
        return '-'
    }

    dayjs.extend(window.dayjs_plugin_duration)

    return `
        <div class="d-flex flex-column">
            <span>${dayjs(consultation.time).format('DD MMM YYYY HH:mm')}</span>
            <span class="text-sm text-secondary" style="font-size: 0.875rem"><i class='bx bx-timer' ></i> ${dayjs.duration(consultation.duration, 'minutes').format('H[h] m[m] s[s]')}</span>
        </div>
    `
}

function showReport(reportId) {
    $('#report-description').text('Loading...')

    $.ajax({
        method: 'GET',
        url: `/api/report/${reportId}`,
        dataType: 'JSON',
        contentType: 'application/json',
        success: (report) => {
            $('#report-description').html(report.description)
        },
        error: (err) => {
            console.log(err);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Failed to load consultant',
            });

            $('#report-description').text('Failed to load report')
        },
    })
}
