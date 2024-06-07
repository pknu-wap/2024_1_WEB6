document.querySelector(".web-title").onclick = () => {
    window.location.href = "../main_page/index.html";
  };

// 닉네임을 URL 인코딩하는 함수
function encodeNickname(nickname) {
    return encodeURIComponent(nickname);
}

// 사용자 닉네임에 기반한 리뷰 데이터를 가져오는 함수
async function fetchReviews(nickname) {
    var encodedNickname = encodeNickname(nickname);

    try {
        const response = await fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/writer/${encodedNickname}/reviews`, {
             method: 'GET',
             credentials: 'include',
             headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        
        const data = await response.json();
        displayReviews(data);

    } catch (error) {
        console.error('Error fetching reviews:', error);
    }
}

// 리뷰 데이터를 화면에 표시하는 함수
function displayReviews(response) {
    var reviewList = document.getElementById("review-list");
    reviewList.innerHTML = ''; // 기존 내용을 지움

    document.getElementById("count_review").textContent = response.data.length;

    if (response.data.length === 0) {
        reviewList.innerHTML = '<p class="no-reviews">작성한 리뷰가 없습니다! 첫 리뷰를 작성해보세요!</p>';
        return;
    }

    response.data.forEach(article => {
        var reviewItem = document.createElement("div");
        reviewItem.className = "review-item";

        var movieContainer = document.createElement("div");
        movieContainer.className = "movie-container";
        
        var reviewTitle = document.createElement("a");
        reviewTitle.className = "review-title";
        reviewTitle.textContent = article.title;
        reviewTitle.href = `../detail_page/detailpage.html?movieId=${article.movieId}&movieSeq=${article.movieSeq}`;
        var reviewRating = document.createElement("div");
        reviewRating.className = "review-rating";
        reviewRating.textContent = `★${article.grade}`;
        
        movieContainer.appendChild(reviewTitle);
        movieContainer.appendChild(reviewRating);

        var reviewDetails = document.createElement("div");
        reviewDetails.className = "review-details";
        reviewDetails.innerHTML = `
            <p style="color: black;">작성자: ${article.review.nickname}</p>
            <p>★ ${article.review.grade}</p>
            ${article.review.edit ? '<p class="additional-information">수정됨</p>' : ''}
            ${article.review.spoiler ? '<p class="additional-information">스포주의</p>' : ''}
            <p class="comment-count" data-review-id="${article.review.id}" style="cursor:pointer; color:blue;">대댓글 ${article.review.commentsCount}</p> <!-- 대댓글 클릭 이벤트 추가 -->
        `;

        var reviewDate = document.createElement("p");
        reviewDate.className = "review-date";
        reviewDate.textContent = `작성일시: ${new Date(article.review.createDate).toLocaleString()}`;

        var reviewContentContainer = document.createElement("div");
        reviewContentContainer.className = "review-content-container";
        

        var reviewContent = document.createElement("div");
        reviewContent.className = "review-content";
        reviewContent.textContent = article.review.content;

        reviewContentContainer.appendChild(reviewContent);
        reviewContentContainer.appendChild(reviewDetails.querySelector('.comment-count')); // 대댓글 개수를 review-content-container로 이동

        reviewItem.appendChild(movieContainer);
        reviewItem.appendChild(reviewDate);
        reviewItem.appendChild(reviewDetails);
        reviewItem.appendChild(reviewContentContainer);

        reviewList.appendChild(reviewItem);
    });

document.querySelectorAll('.comment-count').forEach(element => {
    element.addEventListener('click', function () {
        var reviewId = this.getAttribute('data-review-id');
        window.location.href = `../detail_page/comment/comment.html?review-id=${reviewId}`;
    });
});
}


// 페이지가 로드될 때 리뷰 데이터를 가져옴
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const nickname = urlParams.get('nickname');
    if (nickname) {
        fetchReviews(nickname);
    } else {
        console.error('닉네임을 찾을 수 없습니다.');
    }
});