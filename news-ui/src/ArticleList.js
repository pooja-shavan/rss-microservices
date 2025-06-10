import React from "react";
import PropTypes from "prop-types";
import "./ArticleList.css";

const openInNewTab = url => window.open(url, "_blank");
const clickableStyle = { cursor: "pointer" };

function ArticleList({ articles }) {
    return (
        <div className="article-list">
            {articles.map(({ id, imageUrl, articleUrl, title, description, content, author, sourceName, publishedAt, fetchedAt }) => (
                <article className="article-card" key={id}>
                    <img
                        src={imageUrl}
                        alt={title}
                        className="article-img"
                        onClick={() => openInNewTab(articleUrl)}
                        style={clickableStyle}
                        onError={e => { e.target.src = "/placeholder.png"; }}
                    />
                    <h2
                        className="article-title"
                        onClick={() => openInNewTab(articleUrl)}
                        style={{ ...clickableStyle, fontSize: "1.3rem" }}
                    >
                        {title}
                    </h2>
                    <p
                        className="article-desc"
                        onClick={() => openInNewTab(articleUrl)}
                        style={{ ...clickableStyle, fontSize: "1rem" }}
                    >
                        {description}
                    </p>
                    <p
                        className="article-desc"
                        onClick={() => openInNewTab(articleUrl)}
                        style={{ ...clickableStyle, fontSize: "1rem" }}
                    >
                        {content}
                    </p>
                    <div className="article-meta" style={{ fontSize: "0.9rem" }}>
                        <span>{author}</span> | <span>{sourceName}</span>
                    </div>
                    <div className="article-meta" style={{ fontSize: "0.9rem" }}>
                        <span>{publishedAt}</span> | <span>{fetchedAt}</span>
                    </div>
                </article>
            ))}
        </div>
    );
}

ArticleList.propTypes = {
    articles: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
            imageUrl: PropTypes.string,
            articleUrl: PropTypes.string.isRequired,
            title: PropTypes.string.isRequired,
            description: PropTypes.string,
            content: PropTypes.string,
            author: PropTypes.string,
            sourceName: PropTypes.string,
            publishedAt: PropTypes.string,
            fetchedAt: PropTypes.string,
        })
    ).isRequired,
};

export default ArticleList;