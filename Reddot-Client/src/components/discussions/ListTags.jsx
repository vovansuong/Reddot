import { Card } from "react-bootstrap";
import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import PropTypes from "prop-types";

//Service
import { viewAllTag } from "../../services/tagService/tagService";

const ListTags = (props) => {
  const { handleUpdateDiscussion } = props;
  //All Tags
  const [listTags, setListTags] = useState([]);

  const getAllTagsData = async () => {
    const res = await viewAllTag(1, 10, "id", "ASC", "");
    if (res && res.data.length > 0) {
      setListTags(res.data);
      toast.success(res?.data?.message);
    } else {
      toast.error(res?.data?.message);
    }
  };

  useEffect(() => {
    getAllTagsData();
  }, []);

  return (
    <Card>
      <Card.Header>
        <Card.Title as="h5">List Tags</Card.Title>
      </Card.Header>
      <div style={{ display: "flex", flexWrap: "wrap", width: "100%" }}>
        {listTags.map((tag) => (
          <div
            key={tag.id}
            className="d-flex justify-content-between align-items-start my-1"
          >
            <button
              style={{
                backgroundColor: tag.color,
                padding: "5px 8px",
                borderRadius: "5px",
                color: "white",
              }}
              onClick={() => handleUpdateDiscussion(tag.id)}
            >
              <i className="fa-solid fa-tag"></i> {tag?.label}
            </button>
          </div>
        ))}
      </div>
    </Card>
  );
};

ListTags.propTypes = {
  handleUpdateDiscussion: PropTypes.func.isRequired,
};

export default ListTags;
