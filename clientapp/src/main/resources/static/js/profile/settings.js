$(function () {
    $('#input-photo').on('change', function () {
        readURL(this)
    })

    $("#form-profile").on("submit", function () {
      $("body").block({
          message: '<div class="spinner-border text-primary" role="status"></div>',
          css: {
              backgroundColor: "transparent",
              border: "0",
          },
          overlayCSS: {
              backgroundColor: "#000",
              opacity: 0.25,
          }
      })
  })
})

function readURL(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            $('#img-photo').attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    } else {
        console.log(profile.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed=${profile.name}`)
        $('#img-photo').attr('src', profile.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed=${profile.name}`);
    }
}
