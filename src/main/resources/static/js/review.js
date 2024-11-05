function showWritableReviews() {
    document.getElementById("writable-review-tab").classList.add("active");
    document.getElementById("written-review-tab").classList.remove("active");
    document.getElementById("writable-reviews").style.display = "block";
    document.getElementById("written-reviews").style.display = "none";
  }

  function showWrittenReviews() {
    document.getElementById("written-review-tab").classList.add("active");
    document.getElementById("writable-review-tab").classList.remove("active");
    document.getElementById("writable-reviews").style.display = "none";
    document.getElementById("written-reviews").style.display = "block";
  }