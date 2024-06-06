// 제목 클릭 시 메인 페이지로 이동
var elements = document.getElementsByClassName('web-title');
elements[0].onclick = function () {
    window.location.href = '/main_page/index.html';
};


const dummy = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fpng.pngtree.com%2Felement_our%2F20190529%2Fourlarge%2Fpngtree-black-movie-field-board-illustration-image_1221631.jpg&type=sc960_832";

document.addEventListener("DOMContentLoaded", function () {
    const movieseq = getQueryParam('movieseq');
    const movieid = getQueryParam('movieid');

    getMovieDetail(movieid, movieseq);
    getMovieReviews(movieid, movieseq);

    document.getElementById("submitComment").addEventListener("click", function () {
        submitComment(movieid, movieseq);
    });
});

function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

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


// 댓글 작성 버튼 클릭 이벤트 핸들러
async function submitComment(movieid, movieseq) {
    const commentText = document.getElementById("commentInput").value;
    const username = "사용자";

    const url = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/movies/' + movieid + '/' + movieseq + '/reviews';

    canWrite = await getCanWrite(url);

    if(!canWrite){return;}
    else if(commentText.trim() !== ""){alert("댓글을 작성해 주세요.")}
    else {

        const data = {
            content: commentText,
            grade : 5,
            spoiler : false
        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data),
            credentials : 'include'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
            const newCommentDiv = document.createElement("div");
            newCommentDiv.classList.add("comment");
            newCommentDiv.innerHTML = `
                <p><strong>${username}</strong> (방금)</p>
                <p>${commentText}</p>
            `;
            document.getElementById("comments").appendChild(newCommentDiv);
            document.getElementById("commentInput").value = ""; // 댓글 입력란 초기화
            alert('Comment submitted successfully!');
            window.location.reload();
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            alert('Failed to submit comment');
        });
    }
}

async function getCanWrite(url){
    await fetch(url, {
        method: 'GET',
        credentials : 'include'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        if(!data.success){
            alert(data.message);
            return;
        }
        canWrite=true;
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
        alert('Failed to submit comment');
    });
}



// 댓글 최신순으로 불러오기
function getMovieReviews(movieid, movieseq) {
    const requestUrl = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/movies/' + movieid + '/' + movieseq + '/reviewsLatest';
    fetch(requestUrl, {
        credentials: 'include'
    }) 
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 응답을 JavaScript 객체로 변환
        })
        .then(data => {
            const commentsDiv = document.getElementById("comments");
            commentsDiv.innerHTML = ""; // 기존 댓글 초기화
            if(data.data.length == 0 ){
                const nodata = document.createElement ("div");
                nodata.classList.add("comment");
                nodata.innerHTML = '작성된 댓글이 없습니다.';
                commentsDiv.appendChild(nodata);
            }
            else {
                data.data.forEach(review => {
                const reviewElement = document.createElement("div");
                reviewElement.classList.add("comment");
                reviewElement.innerHTML = `
                    <p><strong>${review.user}</strong> (${review.date})</p>
                    <p>${review.content}</p>
                `;
                commentsDiv.appendChild(reviewElement);
                });
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

// 대댓글 순으로 불러오기
function getMovieReviewsByCommentCnt(movieid, movieseq) {
    const requestUrl = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/movies/' + movieid + '/' + movieseq + '/reviewsCommentCnt';
    fetch(requestUrl, {
        credentials: 'include'
    }) 
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 응답을 JavaScript 객체로 변환
        })
        .then(data => {
            const commentsDiv = document.getElementById("comments");
            commentsDiv.innerHTML = ""; // 기존 댓글 초기화
            if(data.data.length == 0 ){
                const nodata = document.createElement ("div");
                nodata.classList.add("comment");
                nodata.innerHTML = '작성된 댓글이 없습니다.';
                commentsDiv.appendChild(nodata);
            }
            else {
                data.data.forEach(review => {
                const reviewElement = document.createElement("div");
                reviewElement.classList.add("comment");
                reviewElement.innerHTML = `
                    <p><strong>${review.user}</strong> (${review.date})</p>
                    <p>${review.content}</p>
                `;
                commentsDiv.appendChild(reviewElement);
                });
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
