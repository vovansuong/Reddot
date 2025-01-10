import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import ReactQuill from "react-quill";

import { Button, Col, Row, Card, ListGroup } from "react-bootstrap";

import {
  getEmailOptionById,
  updateEmailOption,
  getAllEmail,
  sendEmail,
} from "../../../services/emailService/EmailOptionService";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";

import SelectMulti from "../selectMulti/SelectMulti";

const EmailOption = () => {
  const [emailOption, setEmailOption] = useState({});

  const [host, setHost] = useState(0);
  const [port, setPort] = useState(0);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [tlsEnable, setTlsEnable] = useState(true);
  const [authentication, setAuthentication] = useState(true);

  const [errHost, setErrHost] = useState("");
  const [errPort, setErrPort] = useState("");
  const [errUsername, setErrUsername] = useState("");
  const [errPass, setErrPass] = useState("");

  const [show, setShow] = useState(false);
  const [showTest, setShowTest] = useState(false);

  const [listEmail, setListEmail] = useState([]);

  let currentUser = useSelector((state) => state.auth.login?.currentUser);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const getEmailOption = async () => {
    const accessToken = currentUser?.accessToken;
    let res = await getEmailOptionById(accessToken, axiosJWT);
    if (+res.status === 200) {
      setEmailOption(res?.data?.data);
      const { host, port, username, password, tlsEnable, authentication } =
        res.data.data;
      setHost(host);
      setPort(port);
      setUsername(username);
      setPassword(password);
      setTlsEnable(tlsEnable);
      setAuthentication(authentication);
    } else {
      console.log(`Error: `, res?.data?.message);
      navigate("/login");
    }

    return true;
  };

  const handleUpdateEmailOption = async () => {
    setErrHost("");
    if (host || host === 0) {
      setErrHost("Please enter host of email!");
    }
    setErrPort("");
    if (port || port === 0) {
      setErrPort("Please enter port of email!");
    }

    setErrUsername("");
    if (username || username === "") {
      setErrUsername("Please enter username of email!");
    }
    setErrPass("");
    if (password || password === "") {
      setErrPass("Please enter password of email");
    }

    if (errHost != "" || errPort != "" || errPass != "" || errUsername != "") {
      return;
    }

    const emailOption = {
      host: host,
      port: +port,
      username: username,
      password: password,
      tlsEnable: tlsEnable,
      authentication: authentication,
    };
    const accessToken = currentUser?.accessToken;
    let res = await updateEmailOption(emailOption, accessToken, axiosJWT);
    console.log(`Check res`, res.data);
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setEmailOption(res.data);
      getEmailOption();
      toast.success("Updated successfully");
      setShow(false);
    } else {
      console.log(`Error: `, res?.data?.message);
    }
  };

  const fetchAllEmail = async () => {
    let res = await getAllEmail(currentUser?.accessToken, axiosJWT);
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setListEmail(res?.data?.data);
    } else {
      console.log(`Error`);
    }
  };

  // Format tags for react-select
  const tagOptions = listEmail?.map((item) => ({
    value: item.email,
    label: item.username,
  }));

  const toolbarOptions = [
    ["bold", "italic", "underline", "strike"], // toggled buttons
    ["blockquote", "code-block"],
    ["link", "image", "video", "formula"],

    [{ header: 1 }, { header: 2 }], // custom button values
    [{ list: "ordered" }, { list: "bullet" }, { list: "check" }],
    [{ script: "sub" }, { script: "super" }], // superscript/subscript
    [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
    [{ direction: "rtl" }], // text direction

    [{ size: ["small", false, "large", "huge"] }], // custom dropdown
    [{ header: [1, 2, 3, 4, 5, 6, false] }],

    [{ color: [] }, { background: [] }], // dropdown with defaults from theme
    [{ font: [] }],
    [{ align: [] }],

    ["clean"], // remove formatting button
  ];

  const module = {
    toolbar: toolbarOptions,
  };

  //Text Mail
  const [subject, setSubject] = useState("");
  const [errFrom, setErrFrom] = useState("");

  const [message, setMessage] = useState("");
  const [errMsg, setErrMsg] = useState("");

  const [selectedEmail, setSelectedEmail] = useState([]);
  const [errTo, setErrTo] = useState("");

  const handleSelectChange = (selectedEmail) => {
    setSelectedEmail(selectedEmail);
  };

  const handleSendEmail = async () => {
    //validation
    setErrFrom("");
    if (subject === "" || subject === null) {
      setErrFrom("Please enter title from email!");
    }

    setErrTo("");
    if (selectedEmail.length <= 0 || selectedEmail === null) {
      setErrTo("Please select from email!");
    }
    setErrMsg("");
    if (message === "" || message === null) {
      setErrMsg("Please enter body of email!");
    }

    if (errFrom === "" && errTo === "" && errMsg === "") {
      const emailOption = {
        emails: selectedEmail.map((item) => item.value),
        subject: subject,
        template: message,
      };

      const accessToken = currentUser?.accessToken;

      let res = await sendEmail(emailOption, accessToken, axiosJWT);
      if (+res?.status === 200 || +res?.data?.status === 200) {
        toast.success("Sent email successfully");
        setShowTest(false);
        setSubject("");
        setMessage("");
        setSelectedEmail([]);
      } else {
        toast.error(res?.data?.message);
      }
    }
  };

  useEffect(() => {
    if (!currentUser.accessToken) {
      createAxios(currentUser, dispatch, loginSuccess);
    }
    getEmailOption();
    fetchAllEmail();
  }, []);

  return (
    <div className="content">
      <h3>Email Option Manage</h3>
      <Col>
        <p></p>
        {emailOption != null ? (
          <ListGroup as="ol" numbered>
            <ListGroup.Item as="li">Host: {emailOption?.host}</ListGroup.Item>
            <ListGroup.Item as="li">Port: {emailOption?.port}</ListGroup.Item>
            <ListGroup.Item as="li">
              Username: {emailOption?.username}
            </ListGroup.Item>
          </ListGroup>
        ) : (
          <div className="text-center">
            <span className="d-flex justify-content-center">
              <i className="fas fa-sync fa-spin fa-5x"></i>
              <br />
            </span>
            <h5>Loading...</h5>
          </div>
        )}

        <Row>
          <div className="d-flex justify-content-between">
            {!showTest && (
              <Button
                style={{ minWidth: 200 }}
                className="col-2 me-3"
                variant="secondary"
                onClick={() => setShowTest(!showTest)}
              >
                {show ? "Close" : "Test"}
              </Button>
            )}
            {!show && (
              <Button
                style={{ minWidth: 200 }}
                className="col-2 me-3"
                variant="info"
                onClick={() => setShow(!show)}
              >
                {show ? "Close" : "Update New Email"}
              </Button>
            )}
          </div>
        </Row>

        {show && (
          <Card className="my-3">
            <h5 className="text-center m-3">Update Email Option</h5>
            <Row className="p-3">
              <div className="form-group mb-3 col-md-6">
                <label htmlFor="host">
                  Host of email: <span className="text-danger">(*)</span>
                </label>
                <input
                  value={host}
                  name="host"
                  className="form-control"
                  required
                  onChange={(e) => setHost(e.target.value)}
                />
                {errHost && errHost != "" && (
                  <small className="text-danger">{errHost}</small>
                )}
              </div>
              <div className="form-group mb-3 col-md-6">
                <label htmlFor="port">
                  Port of email: <span className="text-danger">(*)</span>
                </label>
                <input
                  value={port}
                  type="number"
                  name="port"
                  className="form-control"
                  required
                  onChange={(e) => setPort(e.target.value)}
                />
                {errPort && errPort != "" && (
                  <small className="text-danger">{errPort}</small>
                )}
              </div>
            </Row>
            <Row className="p-3">
              <div className="form-group mb-3 col-md-6">
                <label htmlFor="username">
                  Username of Email <span className="text-danger">(*)</span>
                </label>
                <input
                  value={username}
                  name="username"
                  className="form-control"
                  required
                  onChange={(e) => setUsername(e.target.value)}
                />
                {errUsername && errUsername != "" && (
                  <small className="text-danger">{errUsername}</small>
                )}
              </div>
              <div className="form-group mb-3 col-md-6">
                <label htmlFor="password">
                  Password of email <span className="text-danger">(*)</span>
                </label>
                <input
                  type="password"
                  value={password}
                  name="password"
                  className="form-control"
                  required
                  onChange={(e) => setPassword(e.target.value)}
                />
                {errPass && errPass != "" && (
                  <small className="text-danger">{errPass}</small>
                )}
              </div>
            </Row>
            <Row className="d-flex justify-content-end">
              <button
                style={{ minWidth: 200 }}
                className="btn btn-secondary col-3 me-5"
                onClick={() => setShow(false)}
              >
                Cancel
              </button>
              <Button
                style={{ minWidth: 200 }}
                className="col-3 me-5"
                onClick={handleUpdateEmailOption}
              >
                Update
              </Button>
            </Row>
          </Card>
        )}
      </Col>

      <Col>
        {showTest && (
          <Card>
            <Card.Header>
              <Card.Title as="h5">Text Email Option</Card.Title>
            </Card.Header>
            <Card.Body>
              <div className="row mb-3">
                <div className="col-12 col-lg-6 col-md-6 fw-bold">
                  <label htmlFor="subject" className="ms-2 me-auto form-label">
                    Subject (*):
                  </label>
                  <input
                    type="text"
                    id="subject"
                    className="form-control p-2 me-2"
                    placeholder="Enter here ...."
                    onChange={(e) => setSubject(e.target.value)}
                  />
                  {errFrom && errFrom != "" && (
                    <small className="text-danger">{errFrom}</small>
                  )}
                </div>
                <div className="col-12 col-lg-6 col-md-6  fw-bold">
                  <label htmlFor="" className="ms-2 me-auto form-label">
                    To (*):
                  </label>

                  <SelectMulti
                    tagOptions={tagOptions}
                    selectedTags={selectedEmail}
                    handleTagChange={handleSelectChange}
                  />

                  {errTo && errTo != "" && (
                    <small className="text-danger">{errTo}</small>
                  )}
                </div>
              </div>
              <div className="row">
                <div className="col-12">
                  <label
                    htmlFor="message"
                    className="ms-2 me-auto label-control"
                  >
                    Message (*):
                  </label>
                  <ReactQuill
                    theme="snow"
                    modules={module}
                    value={message}
                    onChange={(value) => setMessage(value)}
                    id="message"
                    placeholder="Enter message here ...."
                    className="content-editor"
                  />
                  {errMsg && errMsg != "" && (
                    <small className="text-danger">{errMsg}</small>
                  )}
                </div>
              </div>
              <div className="d-flex justify-content-end">
                <button
                  style={{ minWidth: 200 }}
                  className="btn btn-secondary col-md-2 me-5"
                  onClick={() => setShowTest(false)}
                >
                  Cancel
                </button>

                <button
                  style={{ minWidth: 200 }}
                  className="col-md-2 me-3 btn btn-group-vertical align-items-center btn-primary"
                  onClick={handleSendEmail}
                >
                  Send
                </button>
              </div>
            </Card.Body>
          </Card>
        )}
      </Col>
    </div>
  );
};

export default EmailOption;
