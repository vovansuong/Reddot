import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";

import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";

import { Col, Row, Card, ListGroup, Button } from "react-bootstrap";

import {
  getAvatarOption,
  updateAvatarOption,
} from "../../../services/configService/configService";

const ConfigAvatar = () => {
  const [maxFileSize, setMaxFileSize] = useState(5);
  const [maxHeight, setMaxHeight] = useState(800);
  const [maxWidth, setMaxWidth] = useState(800);

  const [errMaxSize, setErrMaxSize] = useState("");
  const [errHeight, setErrHeight] = useState("");
  const [errWidth, setErrWidth] = useState("");

  const [avatarOption, setAvatarOption] = useState({});

  const [showEdit, setShowEdit] = useState(false);

  let currentUser = useSelector((state) => state.auth.login?.currentUser);

  const dispatch = useDispatch();
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const fetchAvatarOption = async () => {
    let res = await getAvatarOption(axiosJWT, currentUser?.accessToken);
    if (res && +res?.status === 200) {
      let data = res?.data?.data;
      setAvatarOption(res?.data?.data);
      setMaxFileSize(data.maxFileSize);
      setMaxHeight(data.maxHeight);
      setMaxWidth(data.maxWidth);
    } else {
      console.log("Error");
    }
  };

  useEffect(() => {
    if (maxFileSize === null) {
      setErrMaxSize("Max File Size is required");
    } else if (+maxFileSize < 100 || +maxFileSize > 1000000) {
      setErrMaxSize("Max File Size must be between 100 and 1000000");
    } else {
      setErrMaxSize("");
    }
  }, [maxFileSize]);

  useEffect(() => {
    if (maxHeight === null) {
      setErrHeight("Max Height is required");
    } else if (+maxHeight < 100 || maxHeight > 1200) {
      setErrHeight("Max Height must be between 100 and 1200");
    } else {
      setErrHeight("");
    }
  }, [maxHeight]);

  useEffect(() => {
    if (maxWidth === null) {
      setErrWidth("Max Width is required");
    } else if (maxWidth < 100 || maxWidth > 1200) {
      setErrWidth("Max Width must be between 100 and 1200");
    } else {
      setErrWidth("");
    }
  }, [maxWidth]);

  const editAvatarOption = async () => {
    if (errMaxSize !== "" || errHeight !== "" || errWidth !== "") {
      toast.error("Invalid data");
      return;
    }

    let data = {
      maxFileSize: +maxFileSize,
      maxHeight: +maxHeight,
      maxWidth: +maxWidth,
    };
    let res = await updateAvatarOption(
      data,
      axiosJWT,
      currentUser?.accessToken,
    );
    if (res && +res?.status === 200) {
      toast.success("Update success");
      fetchAvatarOption();
      setShowEdit(false);
    } else {
      toast.error("Update failed");
    }
  };

  useEffect(() => {
    fetchAvatarOption();
  }, []);

  return (
    <article className="dashboard content">
      <h1>Avatar Option</h1>

      <section className="mb-3">
        {avatarOption != null ? (
          <ListGroup as="ol" numbered>
            <ListGroup.Item as="li">
              Max File Size: {avatarOption?.maxFileSize} KB
            </ListGroup.Item>
            <ListGroup.Item as="li">
              Max Height: {avatarOption?.maxHeight} px
            </ListGroup.Item>
            <ListGroup.Item as="li">
              Max width: {avatarOption?.maxWidth} px{" "}
            </ListGroup.Item>
          </ListGroup>
        ) : (
          <div className="text-center">
            <span className="d-flex justify-content-center">
              <i className="fas fa-sync fa-spin fa-5x"></i>
              <br />
            </span>
            <h5>No data...</h5>
          </div>
        )}
      </section>

      <section>
        {!showEdit && (
          <Button
            className="col-2 me-3"
            variant="info"
            onClick={() => setShowEdit(!showEdit)}
          >
            {showEdit ? "Close" : "Update"}
          </Button>
        )}
        {showEdit && (
          <Card>
            <Card.Header>
              <h4>Update Avatar Option</h4>
            </Card.Header>
            <Card.Body>
              <Row>
                <Col>
                  <label htmlFor="maxFileSize">Max File Size</label>
                  <input
                    id="maxFileSize"
                    type="number"
                    className="form-control"
                    value={maxFileSize}
                    onChange={(e) => setMaxFileSize(e.target.value)}
                  />
                  {errMaxSize && (
                    <small className="text-danger">{errMaxSize}</small>
                  )}
                </Col>
                <Col>
                  <label htmlFor="maxHeight">Max Height</label>
                  <input
                    id="maxHeight"
                    type="number"
                    className="form-control"
                    value={maxHeight}
                    onChange={(e) => setMaxHeight(e.target.value)}
                  />
                  {errHeight && (
                    <small className="text-danger">{errHeight}</small>
                  )}
                </Col>
                <Col>
                  <label htmlFor="maxWidth">Max Width</label>
                  <input
                    id="maxWidth"
                    type="number"
                    className="form-control"
                    value={maxWidth}
                    onChange={(e) => setMaxWidth(e.target.value)}
                  />
                  {errWidth && (
                    <small className="text-danger">{errWidth}</small>
                  )}
                </Col>
              </Row>
              <Row>
                <Col>
                  <button
                    className="btn btn-secondary col-md-2 me-5"
                    onClick={() => setShowEdit(false)}
                  >
                    Cancel
                  </button>
                </Col>
                <Col>
                  <button
                    className="btn btn-primary"
                    onClick={editAvatarOption}
                  >
                    Update
                  </button>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        )}
      </section>
    </article>
  );
};

export default ConfigAvatar;
