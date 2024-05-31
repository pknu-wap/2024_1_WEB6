document.addEventListener("DOMContentLoaded", function() {
    fetchMovies();
});

function fetchMovies() {
    fetch('http://localhost:8080//movies/latest')// 일단 최신순 정렬 하나만 적어놓음. 댓글순도 추가하기.
        .then(response => response.json())
        .then(data => {
            displayMovies(data);
            console.log(JSON.stringify(data)); // JSON 형식으로 출력
        })
        .catch(error => console.error("Error fetching movies:", error));
}

function displayMovies(movies) {
    const moviesList = document.getElementById("moviesList");
    moviesList.innerHTML = "";

    movies.forEach(movie => {
        const li = document.createElement("li");
        li.innerHTML = `<h3><a href="#" onclick="handleClick(${movie.movieSeq})">${movie.title} (${movie.prodYear})</a></h3>`;
        moviesList.appendChild(li);
    });
}

function handleClick(movieSeq) {
    const apiUrl =  `http://localhost:8080/movies/detail/${movieSeq}`;
    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ movieSeq }), // movieSeq를 JSON 형식으로 변환하여 서버로 전송
    };

    fetch(apiUrl, options)
        .then((response) => response.json())
        .then((data) => {
            console.log("Movie detail response:", data); // 서버에서 받은 데이터를 콘솔에 출력
            // 받은 데이터를 활용하여 상세 정보를 표시하거나 다른 작업 수행
        })
        .catch((error) => {
            console.error("Error fetching movie detail:", error);
        });
}