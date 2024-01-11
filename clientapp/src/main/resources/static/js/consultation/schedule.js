$(function () {
    const calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
        initialView: "dayGridMonth",
        schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
        },
        googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE',

        // US Holidays
        events: {
            googleCalendarId: 'en.indonesian#holiday@group.v.calendar.google.com',
            className: 'gcal-event' // an option!
        },
        eventClick: function (info) {
            console.log(info.event.extendedProps.consultation)
        },
        eventClassNames: function (info) {
            const consultation = info.event.extendedProps.consultation
            if (!consultation && info.event.title) {
                return ["fc-event-danger"]
            }

            return ["fc-event-primary"]
        },
        eventDidMount: function () {
            $(".tooltip-avatar").tooltip();
        },
        eventContent: function (info) {
            const consultation = info.event.extendedProps.consultation
            if (!consultation && info.event.title) {
                return {
                    html: `<span class="w-100 text-truncate d-block">${info.event.title}</span>`
                }
            }

            return {
                html: `
                    <div 
                      class="cursor-pointer w-100 d-flex gap-1 align-items-center"
                        data-bs-toggle="modal" 
                        data-bs-target="#modal-detail"
                        onclick="showDetail(${consultation.id})"
                      >
                          <ul class="list-unstyled users-list m-0 avatar-group d-flex align-items-center">
                            <li
                              data-bs-toggle="tooltip"
                              data-popup="tooltip-custom"
                              data-bs-placement="top"
                              class="tooltip-avatar avatar avatar-xs pull-up"
                              title="${consultation.counselee.name} as counselee"
                            >
                              <img 
                                src="${consultation.counselee.photo || `https://api.dicebear.com/7.x/fun-emoji/svg?seed=${consultation.counselee.name}`}" 
                                alt="${consultation.counselee.name}"
                                class="rounded-circle" 
                              />
                            </li>
                            <li
                              data-bs-toggle="tooltip"
                              data-popup="tooltip-custom"
                              data-bs-placement="top"
                              class="tooltip-avatar avatar avatar-xs pull-up"
                              title="${consultation.consultant.name} as consultant"
                            >
                              <img 
                                src="${consultation.consultant.photo || `https://api.dicebear.com/7.x/fun-emoji/svg?seed=${consultation.consultant.name}`}" 
                                alt="${consultation.counselee.name}"
                                class="rounded-circle" 
                              />
                            </li>
                          </ul>
                          <span>${consultation.counselee.name}</span>
                    </div>
                `
            }
        }
    })

    calendar.render()

    $.ajax({
        method: 'GET',
        url: '/api/consultations',
        success: (consultations) => {
            consultations.forEach((consultation) => {
                calendar.addEvent({
                    id: consultation.id,
                    title: consultation.counselee.name,
                    start: new Date(consultation.time),
                    end: new Date(new Date(consultation.time).getTime() + consultation.duration * 60000),
                    consultation: consultation
                });
            })
        },
        error: () => {

        }
    })
})
