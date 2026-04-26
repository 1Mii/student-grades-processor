import { useState } from "react";
import "./SearchStudent.css";

function SearchStudent({ handleSearch }) {
  const [input, setInput] = useState("");
  const [searchType, setSearchType] = useState("nmec");

  const onSubmit = (e) => {
    e.preventDefault();
    handleSearch({ type: searchType, value: input });
  };

  return (
    <div className="search-card">
      <form onSubmit={onSubmit} className="search-form">
        <div className="search-field search-field--type">
          <label>Find by</label>
          <select
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
          >
            <option value="id">ID</option>
            <option value="nmec">NMEC</option>
          </select>
        </div>

        <div className="search-field">
          <label>Search</label>
          <input
            type="number"
            placeholder={`${searchType.toUpperCase()}...`}
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
        </div>

        <button type="submit" className="search-button">
          Search
        </button>
      </form>
    </div>
  );
}

export default SearchStudent;