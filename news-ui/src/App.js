import React, { useEffect, useState } from "react";
import Header from "./Header";
import ArticleList from "./ArticleList";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

function App() {
    const [articles, setArticles] = useState([]);


    useEffect(() => {
        fetch(`${API_URL}/api/news`)
            .then(res => res.json())
            .then(data => {
                console.log("API response:", data.articles)
                setArticles(data.articles)
            })
            .catch(() => setArticles([]));
    }, []);

    return (

        <div>
            <Header />
            <main style={{ marginTop: 80 }}>
                <ArticleList articles={articles}  />
            </main>
        </div>
    );
}

export default App;