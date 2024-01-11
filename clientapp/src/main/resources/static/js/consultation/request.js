$(function () {
    $('#form-request').on('submit', function (e) {
        e.preventDefault()

        Swal.fire({
            title: "Are you sure?",
            text: "You want to send consultation request?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#696cff",
            cancelButtonColor: "#ff3e1d",
            confirmButtonText: "Yes!"
        }).then((result) => {
            if (result.isConfirmed) e.target.submit()
        });
    })
})
