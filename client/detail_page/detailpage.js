// 제목 클릭 시 메인 페이지로 이동
var elements = document.getElementsByClassName('web-title');
elements[0].onclick = function () {
    window.location.href = 'https://www.google.com';
};

// 댓글 작성 버튼 클릭 이벤트 핸들러
document.getElementById("submitComment").addEventListener("click", function () {
    const commentText = document.getElementById("commentInput").value;
    if (commentText.trim() !== "") {
        const newComment = new Comment(nextCommentId++, commentText, null);
        comments.push(newComment);
        renderComments(buildCommentTree(comments), document.getElementById("comments"));
        document.getElementById("commentInput").value = ""; // 댓글 입력란 초기화
    } else {
        alert("댓글을 작성해주세요.");
    }
});

// 영화 정보 설정 (예시 데이터)
document.getElementById("title").textContent = "영화 제목";
document.getElementById("year").textContent = "제작년도: 2024";
document.getElementById("actors").textContent = "배우: 배우1, 배우2";
document.getElementById("country").textContent = "국가: 한국";
document.getElementById("summary").textContent = "줄거리: 이 영화는...";
document.getElementById("runtime").textContent = "런타임: 120분";
document.getElementById("rating").textContent = "관람가: 12세 이상";
document.getElementById("genre").textContent = "장르: 드라마";

document.addEventListener("DOMContentLoaded", function () {
    var comments = [];
    var nextCommentId = 1;

    class Comment {
        constructor(commentId, content, parentId) {
            this.commentId = commentId;
            this.content = content;
            this.parentId = parentId;
            this.replies = [];
            this.timestamp = new Date();
            this.isSpoiler = false;
        }

        addReply(reply) {
            this.replies.push(reply);
        }

        getTotalReplies() {
            let totalReplies = this.replies.length;
            for (let reply of this.replies) {
                totalReplies += reply.getTotalReplies();
            }
            return totalReplies;
        }
    }

    function buildCommentTree(comments) {
        const commentDict = {};
        const rootComments = [];

        comments.forEach(comment => {
            commentDict[comment.commentId] = comment;
        });

        comments.forEach(comment => {
            if (comment.parentId) {
                const parentComment = commentDict[comment.parentId];
                parentComment.addReply(comment);
            } else {
                rootComments.push(comment);
            }
        });

        return rootComments;
    }

    function sortCommentsByLatest(comments) {
        comments.sort((a, b) => b.timestamp - a.timestamp);
        comments.forEach(comment => sortCommentsByLatest(comment.replies));
    }

    function sortCommentsByReplies(comments) {
        comments.sort((a, b) => b.getTotalReplies() - a.getTotalReplies());
        comments.forEach(comment => sortCommentsByReplies(comment.replies));
    }

    function renderComment(comment, container) {
        const commentDiv = document.createElement("div");
        commentDiv.classList.add("comment");

        const contentDiv = document.createElement("div");
        contentDiv.textContent = comment.content;
        if (comment.isSpoiler) {
            contentDiv.classList.add("spoiler");
        }
        contentDiv.addEventListener("click", () => {
            if (comment.isSpoiler) {
                contentDiv.classList.remove("spoiler");
            }
        });

        const timestampDiv = document.createElement("div");
        timestampDiv.classList.add("timestamp");
        timestampDiv.textContent = `작성 시간: ${comment.timestamp.toLocaleString()}`;

        const replyCountDiv = document.createElement("div");
        replyCountDiv.classList.add("reply-count");
        replyCountDiv.textContent = `답글 수: ${comment.getTotalReplies()}`;

        commentDiv.appendChild(contentDiv);
        commentDiv.appendChild(timestampDiv);
        commentDiv.appendChild(replyCountDiv);

        // 댓글에만 답글, 수정, 삭제, 스포일러 버튼 추가
        if (comment.parentId === null) {
            const replyButton = document.createElement("span");
            replyButton.classList.add("reply-button");
            replyButton.textContent = "답글";
            replyButton.setAttribute("data-id", comment.commentId);
            replyButton.addEventListener("click", () => showReplyInput(commentDiv, comment.commentId));
            commentDiv.appendChild(replyButton);

            const editButton = document.createElement("span");
            editButton.classList.add("edit-button");
            editButton.textContent = "수정";
            editButton.setAttribute("data-id", comment.commentId);
            editButton.addEventListener("click", () => editComment(contentDiv, comment.commentId));
            commentDiv.appendChild(editButton);

            const deleteButton = document.createElement("span");
            deleteButton.classList.add("delete-button");
            deleteButton.textContent = "삭제";
            deleteButton.setAttribute("data-id", comment.commentId);
            deleteButton.addEventListener("click", () => deleteComment(comment.commentId));
            commentDiv.appendChild(deleteButton);

            const spoilerButton = document.createElement("span");
            spoilerButton.classList.add("spoiler-button");
            spoilerButton.textContent = "스포 방지";
            spoilerButton.setAttribute("data-id", comment.commentId);
            spoilerButton.addEventListener("click", () => toggleSpoiler(contentDiv, comment.commentId));
            commentDiv.appendChild(spoilerButton);
        }

        container.appendChild(commentDiv);

        if (comment.replies.length > 0) {
            const repliesContainer = document.createElement("div");
            repliesContainer.classList.add("replies-container");
            comment.replies.forEach(reply => renderComment(reply, repliesContainer));
            commentDiv.appendChild(repliesContainer);
        }
    }

    function renderComments(comments, container) {
        container.innerHTML = "";
        comments.forEach(comment => renderComment(comment, container));
    }

    function showReplyInput(parentDiv, parentId) {
        // 이미 존재하는 답글 입력란이 있으면 삭제
        const existingReplySection = parentDiv.querySelector(".reply-section");
        if (existingReplySection) {
            existingReplySection.remove();
        }

        const replySection = document.createElement("div");
        replySection.classList.add("reply-section");
        const replyInput = document.createElement("textarea");
        replyInput.placeholder = "답글을 작성해주세요";
        const replyButton = document.createElement("button");
        replyButton.textContent = "답글 작성";
        replyButton.addEventListener("click", () => {
            const replyText = replyInput.value.trim();
            if (replyText.length >= 1 && replyText.length <= 300) {
                const newReply = new Comment(nextCommentId++, replyText, parentId);
                comments.push(newReply);
                const parentComment = comments.find(comment => comment.commentId === parentId);
                if (parentComment) {
                    parentComment.addReply(newReply);
                    renderComment(newReply, parentDiv.querySelector(".replies-container") || parentDiv);
                }
                replySection.remove();
            } else {
                alert("답글은 1자 이상 300자 이하로 작성해주세요.");
            }
        });
        replySection.appendChild(replyInput);
        replySection.appendChild(replyButton);
        parentDiv.appendChild(replySection);
    }

    function editComment(contentDiv, commentId) {
        const comment = comments.find(comment => comment.commentId === commentId);
        if (comment) {
            const editText = prompt("댓글을 수정해주세요:", comment.content);
            if (editText !== null && editText.trim().length >= 1 && editText.trim().length <= 500) {
                comment.content = editText.trim();
                contentDiv.textContent = comment.content;
            } else {
                alert("댓글은 1자 이상 500자 이하로 작성해주세요.");
            }
        }
    }

    function deleteComment(commentId) {
        const commentIndex = comments.findIndex(comment => comment.commentId === commentId);
        if (commentIndex !== -1) {
            comments.splice(commentIndex, 1);
            renderComments(buildCommentTree(comments), document.getElementById("comments"));
        }
    }

    function toggleSpoiler(contentDiv, commentId) {
        const comment = comments.find(comment => comment.commentId === commentId);
        if (comment) {
            comment.isSpoiler = !comment.isSpoiler;
            if (comment.isSpoiler) {
                contentDiv.classList.add("spoiler");
            } else {
                contentDiv.classList.remove("spoiler");
            }
        }
    }

    document.getElementById("submitComment").addEventListener("click", function () {
        const commentText = document.getElementById("commentInput").value.trim();
        if (commentText.length >= 15 && commentText.length <= 500) {
            const newComment = new Comment(nextCommentId++, commentText, null);
            comments.push(newComment);
            document.getElementById("commentInput").value = ""; // 댓글 입력란 초기화

            const sortOption = document.getElementById("sortOptions").value;
            let sortedComments = buildCommentTree(comments);
            if (sortOption === "latest") {
                sortCommentsByLatest(sortedComments);
            } else if (sortOption === "replies") {
                sortCommentsByReplies(sortedComments);
            }

            renderComments(sortedComments, document.getElementById("comments"));
        } else {
            alert("댓글은 15자 이상 500자 이하로 작성해주세요.");
        }
    });

    document.getElementById("sortOptions").addEventListener("change", function () {
        const sortOption = this.value;
        let sortedComments = buildCommentTree(comments);
        if (sortOption === "latest") {
            sortCommentsByLatest(sortedComments);
        } else if (sortOption === "replies") {
            sortCommentsByReplies(sortedComments);
        }
        renderComments(sortedComments, document.getElementById("comments"));
    });

    // Initial rendering with latest sort
    renderComments(buildCommentTree(comments), document.getElementById("comments"));
});
