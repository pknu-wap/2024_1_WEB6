// 제목 클릭 시 메인 페이지로 이동
var elements = document.getElementsByClassName('web-title');
elements[0].onclick = function () {
    window.location.href = '/main_page/index.html';
};


const dummy = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fpng.pngtree.com%2Felement_our%2F20190529%2Fourlarge%2Fpngtree-black-movie-field-board-illustration-image_1221631.jpg&type=sc960_832";

document.addEventListener("DOMContentLoaded", function () {
    const movieSeq = getQueryParam('movieSeq');
    const movieid = getQueryParam('movieid');

    getMovieDetail(movieid, movieSeq);
    getMovieReviews(movieid, movieSeq);

    document.getElementById("submitComment").addEventListener("click", function () {
        submitComment(movieid, movieSeq);
    });
});

function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

function getMovieDetail(movieid, movieSeq){
    fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/detail/'+ movieid +'/'+ movieSeq, {
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
    const grade = document.getElementById('grade').value;
    const checkbox = document.getElementById('spoiler').value;
    if(checkbox=='on') spoiler = true;
    else spoiler = false; 

    const url = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/movies/' + movieid + '/' + movieseq + '/reviews';

    canWrite = await getCanWrite(url);

    if(!canWrite){return;}
    else if(commentText.trim() === ""){alert("댓글을 작성해 주세요.")}
    else {
        const data = {
            content: commentText,
            grade : grade,
            spoiler : spoiler
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
            if(!data.success){
                alert(data.message);
            }
            alert(data.message);
            window.location.reload();
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            alert('Failed to submit comment');
        });
    }
}

async function getCanWrite(url){ //중요
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();

        if (!data.success) {
            alert(data.message);
            return false;
        } else {
            return true;
        }
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        alert('Failed to check if can write comment');
        return false; // 에러가 발생했을 때 false를 반환
    }
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
                    if(review.spoiler){isSpoiler = 'spoiler';}
                    else{isSpoiler='';}
                    const reviewElement = document.createElement("div");
                    reviewElement.id=review.id;
                    reviewElement.classList.add("comment");
                    reviewElement.innerHTML = `
                        <p>
                            <strong>${review.nickname}</strong> (${review.createDate}) ★${review.grade}
                            <div class="edit-button" data-reviewid=${review.id} onclick="editReview(event)" >수정</div>
                            <div class="delete-button" data-reviewid=${review.id} onclick="deleteReview(event)">삭제</div>
                        </p>
                        <p id="reviewContent${review.id}" class="${isSpoiler}">${review.content}</p>
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
function getMovieReviewsByCommentCnt(movieid, movieSeq) {
    const requestUrl = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/movies/' + movieid + '/' + movieSeq + '/reviewsCommentCnt';
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


async function deleteReview(event){
    const tag = event.target;
    const reviewid = tag.getAttribute('data-reviewid');

    const url = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/reviewDelete/'+reviewid;

    try {
        const response = await fetch(url, {
            method: 'DELETE',
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();

        if (data.success) {
            alert(data.message);
            window.location.reload();
        } else{
            alert('리뷰 삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return false; // 에러가 발생했을 때 false를 반환
    }
}

async function editReview(event){
    const tag = event.target;
    const reviewid = tag.getAttribute('data-reviewid');
    const reviewField = document.getElementById(reviewid);

    currentText =  document.getElementById('reviewContent'+reviewid).innerHTML;
    reviewField.innerHTML = `
        <textarea id="reviewEditTextarea" class="review-textarea" placeholder=${currentText}></textarea>
        <button class="edit-process-button" onclick="editProcessReview(${reviewid})">수정 완료</button>
    `;
}

async function editProcessReview(reviewid){

    var textarea = document.getElementById('reviewEditTextarea');
    var text = textarea.value;
    const url = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/reviewEdit/'+reviewid;

    const data = {
        content: text,
        grade : 5,
        spoiler : false
    };

    fetch(url, {
        method: 'PUT',
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
        if(!data.success){
            alert(data.message);
        }
        console.log(data);
        window.location.reload();
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
        alert('Failed to submit comment');
    });
}