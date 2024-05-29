// 클라이언트 측 수정 필요 (test 용)

document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const movieSeq = urlParams.get("movieSeq");
    fetchMovieDetail(movieSeq);
});

function fetchMovieDetail(movieSeq) {
    console.log(`Fetching movie detail for movieSeq: ${movieSeq}`);
    fetch(`http://localhost:8080/movies/detail/?movieSeq=${encodeURIComponent(movieSeq)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.statusText}`);
            }
            return response.json();
        })
        .then(movie => {
            console.log("Movie detail fetched successfully:", movie);
            document.getElementById("movieTitle").textContent = movie.title;
            document.getElementById("movieYear").textContent = movie.prodYear;
            document.getElementById("movieDirector").textContent = movie.directors;
            document.getElementById("movieActor").textContent = movie.actors;
            document.getElementById("movieNation").textContent = movie.nation;
            document.getElementById("moviePlot").textContent = movie.plotText;
            document.getElementById("movieRuntime").textContent = movie.runtime;
            document.getElementById("movieGenre").textContent = movie.genre;

            // 포스터 이미지 표시
            const postersDiv = document.getElementById("moviePosters");
            postersDiv.innerHTML = "";
            if (movie.posters) {
                const img = document.createElement("img");
                img.src = movie.posters;
                img.alt = movie.title;
                postersDiv.appendChild(img);
            } else {
                postersDiv.textContent = "포스터 이미지가 없습니다.";
            }
        })
        .catch(error => console.error("Error fetching movie detail:", error));
}
