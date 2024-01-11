$(function () {
    $('#form-login').validate({
        rules: {
            username: {
                required: true,
                minlength: 4,
                maxlength: 64
            },
            password: {
                required: true,
                minlength: 4,
                maxlength: 32
            }
        },
        submitHandler: function (form) {
            if ($(form).valid()) {
                $('body').block({
                    message: '<div class="spinner-border text-primary" role="status"></div>',
                    css: {
                        backgroundColor: "transparent",
                        border: "0"
                    },
                    overlayCSS: {
                        backgroundColor: "#000",
                        opacity: 0.25
                    }
                })
                form.submit()
            }
        },
        errorPlacement: function (error, element) {
            $(element).closest('.form-group').append(error)
        }
    })

    const urlParams = new URLSearchParams(window.location.search);
    const isSuccess = urlParams.get('success');
    if (isSuccess) {
        const message = urlParams.get('message') || undefined
        Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Success',
            text: message,
            showConfirmButton: false,
            timer: 2000,
        });
    }
})
