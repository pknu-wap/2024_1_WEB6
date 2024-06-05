// DetailpageTest.js

const dummy = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fpng.pngtree.com%2Felement_our%2F20190529%2Fourlarge%2Fpngtree-black-movie-field-board-illustration-image_1221631.jpg&type=sc960_832";

document.addEventListener("DOMContentLoaded", function () {
    const movieseq = getQueryParam('movieseq');
    const movieid = getQueryParam('movieid');

    getMovieDetail(movieid, movieseq);
    getMovieReviews(movieid, movieseq);
});

function getMovieDetail(movieid, movieseq){
    fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/detail/'+ movieid +'/'+ movieseq, {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 응답을 JavaScript 객체로 변환
        })
        .then(data => {
            const movie = data[0];
            const posterImage = document.getElementById("posterImage");
            const title = document.getElementById("title");
            const year = document.getElementById("year");
            const actors = document.getElementById("actors");
            const country = document.getElementById("country");
            const runtime = document.getElementById("runtime");
            const rating = document.getElementById("rating");
            const genre = document.getElementById("genre");
            const summary = document.getElementById("summary");

            posterImage.src = movie.postersList ? movie.postersList[0] : dummy;
            title.innerHTML += movie.title;
            year.innerHTML += `${movie.prodYear}년`;

            const actorList = movie.actors.actor;
            if(actorList.length === 0 || actorList[0].actorNm === ""){
                actors.innerHTML += "배우 없음";
            } else {
                actors.innerHTML += actorList.map(actor => actor.actorNm).join(', ');
            }

            country.innerHTML += movie.nation;
            summary.innerHTML += movie.plots.plot[0].plotText;
            runtime.innerHTML += `${movie.runtime}분`;
            rating.innerHTML += movie.rating || "전체관람가";
            genre.innerHTML += movie.genre || "복합";
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

function getMovieReviews(movieid, movieseq) {
    const requestUrl = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/movies/' + movieid +'/'+ movieseq + '/reviewsLatest';

    fetch(requestUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 응답을 JavaScript 객체로 변환
        })
        .then(data => {
            const commentsDiv = document.getElementById("comments");
            commentsDiv.innerHTML = ""; // 기존 댓글 초기화
            consokle.log(data);
            data.forEach(review => {
                const reviewElement = document.createElement("div");
                reviewElement.classList.add("review");
                reviewElement.innerHTML = `
                    <p><strong>${review.user}</strong> (${review.date})</p>
                    <p>${review.content}</p>
                `;
                commentsDiv.appendChild(reviewElement);
            });
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
