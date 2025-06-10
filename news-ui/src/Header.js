import React from "react";
import "./Header.css";

function Header() {
    return (
        <header className="sticky-header">
            <h1 style={{fontSize: "2rem", margin: 0}}>
                <center>NYT Technology News</center>
            </h1>
            <div className="language-switcher">
                <span className="language active" data-lang="en">ENG</span> |
                <span className="language" data-lang="es">ESP</span>
            </div>
        </header>
    );
}

export default Header;