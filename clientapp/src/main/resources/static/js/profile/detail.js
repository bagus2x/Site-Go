$(function () {
    if (profile.cv) loadPdf(profile.cv)
})

const defaultState = {
    pdf: null,
    currentPage: 1,
    zoom: 1
}

function loadPdf(url) {
    if (!url) return

    pdfjsLib.getDocument(url).then((pdf) => {
        defaultState.pdf = pdf;
        render();
    });
}

function render() {
    defaultState.pdf.getPage(defaultState.currentPage).then((page) => {

        const canvas = document.getElementById('canvas-cv');
        const ctx = canvas.getContext('2d');
        const viewport = page.getViewport(defaultState.zoom);

        canvas.width = viewport.width;
        canvas.height = viewport.height;

        page.render({
            canvasContext: ctx,
            viewport: viewport
        });
    });
}
