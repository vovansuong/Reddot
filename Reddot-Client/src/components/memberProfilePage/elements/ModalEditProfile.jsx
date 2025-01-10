import { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";

import { Row } from "reactstrap";
import { GENDER, EMAIL_REGEX } from "../../../constants";

const ModalEditProfile = (props) => {
  const { show, handleClose, handleUpdateInfo, user } = props;

  const [username, setUsername] = useState(user?.username);

  const [name, setName] = useState(user?.name ?? "");
  const [validName, setValidName] = useState(null);

  const [address, setAddress] = useState(user?.address ?? "");
  const [validAddress, setValidAddress] = useState(null);

  const [phone, setPhone] = useState(user?.phone ?? "");
  const [validPhone, setValidPhone] = useState(null);

  const [email, setEmail] = useState(user?.email ?? "");
  const [validEmail, setValidEmail] = useState(null);

  const [gender, setGender] = useState(user?.gender ?? GENDER.OTHER);

  const [birthDate, setBirthDate] = useState(user?.birthDate ?? new Date());
  const [validBirth, setValidBirth] = useState(null);

  const [bio, setBio] = useState(user?.bio);

  // const [errMsg, setErrMsg] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleEditProfile = () => {
    setIsLoading(true);
    if (
      !validName ||
      !validEmail ||
      !validAddress ||
      !validBirth ||
      !validPhone
    ) {
      toast.error("Please enter valid information");
      return null;
    }
    let dataEdit = {
      username,
      name,
      address,
      phone,
      email,
      gender,
      birthday: new Date(birthDate),
      bio,
    };
    console.log(`Check dataEdit `, dataEdit);
    handleUpdateInfo(dataEdit);

    setIsLoading(false);
  };

  useEffect(() => {
    setUsername(user?.username);
    setName(user?.name ?? "");
    setAddress(user?.address ?? "");
    setPhone(user?.phone ?? "");
    setEmail(user?.email ?? "");
    setGender(user?.gender ?? GENDER.OTHER);
    setBirthDate(user?.birthDate ?? new Date());
    setBio(user?.bio);
  }, [user]);

  useEffect(() => {
    if (name.length > 0) {
      setValidName(true);
    } else {
      setValidName(false);
    }
  }, [name]);

  useEffect(() => {
    if (address.length > 0) {
      setValidAddress(true);
    } else {
      setValidAddress(false);
    }
  }, [address]);

  useEffect(() => {
    if (phone.length > 0) {
      setValidPhone(true);
    } else {
      setValidPhone(false);
    }
  }, [phone]);

  useEffect(() => {
    if (EMAIL_REGEX.test(email)) {
      setValidEmail(true);
    } else {
      setValidEmail(false);
    }
  }, [email]);

  useEffect(() => {
    let age = new Date().getFullYear() - new Date(birthDate).getFullYear();
    if (age >= 16 && age <= 100) {
      setValidBirth(true);
    } else {
      setValidBirth(false);
    }
  }, [birthDate]);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Edit information {username}</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Row>
          <div className="form-group mb-3 col-md-6">
            <label className="form-label" htmlFor="name">
              Full name<span className="text-danger">(*)</span>
            </label>
            <input
              className="form-control"
              id="name"
              type="text"
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Enter new full name"
            />
            <small
              className={!validName ? "text-danger" : ""}
              role="alert"
              hidden={validName}
            >
              <i className="fa fa-info-circle" aria-hidden="true"></i> Name is
              not empty
            </small>
          </div>
          <div className="form-group mb-3 col-md-6">
            <label className="form-label" htmlFor="title">
              Gender:
            </label>
            <select
              className="form-select"
              name="gender"
              onChange={(event) => setGender(event.target.value)}
              value={gender}
            >
              <option value={GENDER.OTHER}>Other</option>
              <option value={GENDER.MALE}>Male</option>
              <option value={GENDER.FEMALE}>Female</option>
            </select>
          </div>
        </Row>
        <Row>
          <div className="form-group mb-3 col-md-6">
            <label className="form-label" htmlFor="title">
              Email<span className="text-danger">(*)</span>
            </label>
            <input
              className="form-control"
              id="email"
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="Enter new email"
            />
            <small
              className={!validEmail ? "text-danger" : ""}
              role="alert"
              hidden={validEmail}
            >
              <i className="fa fa-info-circle" aria-hidden="true"></i> Please
              enter a valid email address
            </small>
          </div>
          <div className="form-group mb-3 col-md-6">
            <label className="form-label" htmlFor="title">
              Phone number<span className="text-danger">(*)</span>
            </label>
            <input
              className="form-control"
              id="phone"
              type="text"
              value={phone}
              onChange={(event) => setPhone(event.target.value)}
              placeholder="Enter phone number"
            />
            <small
              className={!validPhone ? "text-danger" : ""}
              role="alert"
              hidden={validPhone}
            >
              <i className="fa fa-info-circle" aria-hidden="true"></i> Phone
              number is not empty
            </small>
          </div>
        </Row>

        <Row>
          <div className="form-group mb-3 col-md-6">
            <label className="form-label" htmlFor="title">
              Address<span className="text-danger">(*)</span>
            </label>
            <input
              className="form-control"
              id="name"
              type="text"
              value={address}
              onChange={(event) => setAddress(event.target.value)}
              placeholder="Enter new address"
            />
            <small
              className={!validAddress ? "text-danger" : ""}
              role="alert"
              hidden={validAddress}
            >
              <i className="fa fa-info-circle" aria-hidden="true"></i> Address
              is not empty
            </small>
          </div>
          <div className="form-group mb-3 col-md-6">
            <label className="form-label" htmlFor="title">
              Birthday<span className="text-danger">(*)</span>
            </label>
            <input
              className="form-control"
              id="name"
              type="date"
              onChange={(event) => setBirthDate(event.target.value)}
              value={birthDate}
              placeholder="Enter new address"
            />
            <small
              className={!validBirth ? "text-danger" : ""}
              role="alert"
              hidden={validBirth}
            >
              <i className="fa fa-info-circle" aria-hidden="true"></i> The day
              of birth must be from 16 to 100 years old.
            </small>
          </div>
        </Row>
        <div className="form-group mb-3">
          <label className="form-label" htmlFor="title">
            Bio:
          </label>
          <textarea
            cols={12}
            className="form-control"
            value={bio}
            onChange={(event) => setBio(event.target.value)}
            placeholder="Enter here..."
          ></textarea>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button
          variant="secondary"
          onClick={handleClose}
          className="ml-0 me-auto"
        >
          Close
        </Button>
        <Button
          variant="primary"
          onClick={handleEditProfile}
          className="ml-auto me-0"
        >
          {isLoading && <i className="fas fa-sync fa-spin"></i>}
          &nbsp;Save change
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalEditProfile.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  handleUpdateInfo: PropTypes.func.isRequired,
  user: PropTypes.object.isRequired,
};

export default ModalEditProfile;
