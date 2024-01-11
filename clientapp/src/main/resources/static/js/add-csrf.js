function addCsrfHeader() {
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");
    const csrfToken = $("meta[name='_csrf']").attr("content");

    $(document).ajaxSend(function (_, xhr) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });
}

