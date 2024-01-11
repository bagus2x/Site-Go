const cardEl = document.getElementById('sortable-cards');
Sortable.create(cardEl);

// Fungsi yang dipanggil saat mulai menarik elemen
function drag(event) {
    event.dataTransfer.setData("text", event.target.id);
  }
  
  // Fungsi yang dipanggil saat elemen dijatuhkan
  function drop(event) {
    event.preventDefault();
    var data = event.dataTransfer.getData("text");
    event.target.appendChild(document.getElementById(data));
  }
  
  // Fungsi yang memungkinkan elemen untuk dijatuhkan di atasnya
  function allowDrop(event) {
    event.preventDefault();
  }
  