import PropTypes from "prop-types";
import { useCallback, useState, useEffect, useRef } from "react";
import _debounce from "lodash/debounce";
import { InputGroup, InputGroupText, InputGroupAddon, Input } from "reactstrap";
import _ from "lodash";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "./searchHeader.scss";
import { FaSearch } from "react-icons/fa";

//Services
import {
  searchComment,
  searchForum,
} from "../../services/searchService/SearchService";

const SearchFormHeader = () => {
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState("");

  const [listSearch, setListSearch] = useState([]);

  const [listForums, setListForums] = useState([]);

  const [listComments, setListComments] = useState([]);

  const handleComment = async (value) => {
    let res = await searchComment(value);
    if (res && +res.data.status === 200) {
      let cloneListComment = _.cloneDeep(listComments);
      cloneListComment = res.data.data;
      setListComments(cloneListComment);
    }
  };

  const handleForum = async (value) => {
    let res = await searchForum(value);
    if (res && +res.data.status === 200) {
      let cloneListForums = _.cloneDeep(listForums);
      cloneListForums = res.data.data;
      setListForums(cloneListForums);
    }
  };

  const handleSearch = (value) => {
    if (value.trim()) {
      handleForum(value);
      handleComment(value);
    } else {
      setListSearch([]);
    }
  };

  const debounceFn = useCallback(_debounce(handleSearch, 500), []);

  const handleChange = (event) => {
    const value = event.target.value;
    setKeyword(value);
    setDefaultValue(value);
    debounceFn(value);
  };

  const [focusedIndex, setFocusedIndex] = useState(-1);
  const resultContainer = useRef(null);
  const [showListSearch, setShowListSearch] = useState(false);
  const [defaultValue, setDefaultValue] = useState("");

  const handleSelection = (selectedIndex) => {
    const selectedItem = listSearch[selectedIndex];

    if (!selectedItem) return resetSearchComplete();

    if (selectedItem.type === "forum") {
      navigate(`/forum/${selectedItem.id}`);
      resetSearchComplete();
      setDefaultValue("");
    } else {
      navigate(`/discussion/${selectedItem.discussionId}`);
      resetSearchComplete();
      setDefaultValue("");
    }
  };

  const resetSearchComplete = () => {
    setListSearch([]);
    setFocusedIndex(-1);
    setShowListSearch(false);
    setDefaultValue("");
  };

  const handleKeyDown = (event) => {
    const key = event.key;
    let nextIndexCount = 0;

    if (key === "ArrowDown") {
      nextIndexCount = (focusedIndex + 1) % listSearch.length;
      setDefaultValue(listSearch[nextIndexCount].title);
    }

    if (key === "ArrowUp") {
      nextIndexCount =
        (focusedIndex - 1 + listSearch.length) % listSearch.length;
      setDefaultValue(listSearch[nextIndexCount].title);
    }

    if (key === "Escape") {
      resetSearchComplete();
      setDefaultValue("");
    }

    if (key === "Enter") {
      event.preventDefault();
      handleSelection(focusedIndex);
    }

    setFocusedIndex(nextIndexCount);
  };

  useEffect(() => {
    setListSearch([...listForums, ...listComments]);
  }, [listForums, listComments]);

  useEffect(() => {
    if (!resultContainer.current) return;

    resultContainer.current.scrollIntoView({
      block: "center",
    });
  }, [focusedIndex]);

  useEffect(() => {
    if (listSearch.length > 0) setShowListSearch(true);
    if (listSearch.length <= 0) setShowListSearch(false);
  }, [listSearch]);

  useEffect(() => {
    setDefaultValue(keyword);
  }, [keyword]);

  return (
    <form className="search-form col-sm-6 h-screen flex items-center justify-center relative">
      <div
        tabIndex={1}
        onKeyDown={handleKeyDown}
        onBlur={resetSearchComplete}
        className="w-full position-relative"
      >
        <InputGroup className="no-border">
          <Input
            placeholder="Search..."
            className="w-full px-4 py-2 text-lg rounded-full border-2 border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-300 transition-all duration-300 ease-in-out flex items-center"
            name="search"
            value={defaultValue}
            onChange={handleChange}
            onSelect={() => setShowListSearch(true)}
            prefix={<FaSearch className="text-gray-500 mr-3" />} // Thêm biểu tượng tìm kiếm ở bên trái
          />
          <InputGroupAddon addonType="append">
            <InputGroupText className="" onClick={() => handleSearch(keyword)}>
              <i className="fa-solid fa-magnifying-glass"></i>
            </InputGroupText>
          </InputGroupAddon>
        </InputGroup>
        {showListSearch && (
          <div className="search-results max-h-56 overflow-y-auto">
            {listSearch.map((item, index) => (
              <div
                key={index}
                onMouseDown={() => handleSelection(index)}
                ref={index === focusedIndex ? resultContainer : null}
                style={{
                  backgroundColor:
                    index === focusedIndex ? "rgba(0, 0, 0, 0.1)" : "",
                }}
                className="cursor-pointer hover:black hover:bg-opacity-10 p-2 "
              >
                <Link
                  to={
                    item.type === "forum"
                      ? `/forum/${item.id}`
                      : `/discussion/${item.discussionId}`
                  }
                  style={{
                    textDecoration: "none",
                  }}
                >
                  {item.title}
                </Link>
              </div>
            ))}
          </div>
        )}
      </div>
    </form>
  );
};

SearchFormHeader.propTypes = {
  color: PropTypes.string.isRequired,
};

export default SearchFormHeader;
