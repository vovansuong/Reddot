import {
  FaCode,
  FaLaptopCode,
  FaMobileAlt,
  FaServer,
  FaDatabase,
  FaCloud,
  FaRobot,
  FaMicrochip,
  FaWifi,
  FaSatelliteDish,
  FaGamepad,
  FaChess,
  FaDice,
  FaPuzzlePiece,
  FaPlaystation,
  FaXbox,
  FaSteam,
  FaTwitch,
  FaTag,
  FaTags,
  FaBookmark,
  FaBarcode,
  FaBook,
  FaCertificate,
  FaClipboard,
  FaFileAlt,
  FaFolderOpen,
  FaMoneyBill,
  FaMoneyCheck,
  FaChartLine,
  FaChartBar,
  FaWallet,
  FaShoppingCart,
  FaBalanceScale,
  FaCalculator,
  FaMoneyCheckAlt,
} from "react-icons/fa";

import { IoGameController } from "react-icons/io5";

import { Dropdown } from "react-bootstrap";

import PropTypes from "prop-types";

const SelectIcon = (props) => {
  const { handleSelectIcon, icon } = props;

  const iconOptions = [
    { value: "FaCode", label: "Code", icon: <FaCode /> },
    { value: "FaLaptopCode", label: "Laptop Code", icon: <FaLaptopCode /> },
    { value: "FaMobileAlt", label: "Mobile Alt", icon: <FaMobileAlt /> },
    { value: "FaServer", label: "Server", icon: <FaServer /> },
    { value: "FaDatabase", label: "Database", icon: <FaDatabase /> },
    { value: "FaCloud", label: "Cloud", icon: <FaCloud /> },
    { value: "FaRobot", label: "Robot", icon: <FaRobot /> },
    { value: "FaMicrochip", label: "Microchip", icon: <FaMicrochip /> },
    { value: "FaWifi", label: "Wifi", icon: <FaWifi /> },
    {
      value: "FaSatelliteDish",
      label: "Satellite Dish",
      icon: <FaSatelliteDish />,
    },
    { value: "FaGamepad", label: "Gamepad", icon: <FaGamepad /> },
    { value: "FaChess", label: "Chess", icon: <FaChess /> },
    { value: "FaDice", label: "Dice", icon: <FaDice /> },
    { value: "FaPuzzlePiece", label: "Puzzle Piece", icon: <FaPuzzlePiece /> },
    { value: "FaPlaystation", label: "Playstation", icon: <FaPlaystation /> },
    { value: "FaXbox", label: "Xbox", icon: <FaXbox /> },
    { value: "FaSteam", label: "Steam", icon: <FaSteam /> },
    { value: "FaTwitch", label: "Twitch", icon: <FaTwitch /> },

    { value: "FaTag", label: "Tag", icon: <FaTag /> },
    { value: "FaTags", label: "Tags", icon: <FaTags /> },
    { value: "FaBookmark", label: "Bookmark", icon: <FaBookmark /> },
    { value: "FaBarcode", label: "Barcode", icon: <FaBarcode /> },
    { value: "FaBook", label: "Book", icon: <FaBook /> },
    { value: "FaCertificate", label: "Certificate", icon: <FaCertificate /> },
    { value: "FaClipboard", label: "Clipboard", icon: <FaClipboard /> },
    { value: "FaFileAlt", label: "File Alt", icon: <FaFileAlt /> },
    { value: "FaFolderOpen", label: "Folder Open", icon: <FaFolderOpen /> },
    { value: "FaMoneyBill", label: "Money Bill", icon: <FaMoneyBill /> },
    { value: "FaMoneyCheck", label: "Money Check", icon: <FaMoneyCheck /> },
    { value: "FaChartLine", label: "Chart Line", icon: <FaChartLine /> },
    { value: "FaChartBar", label: "Chart Bar", icon: <FaChartBar /> },
    { value: "FaWallet", label: "Wallet", icon: <FaWallet /> },
    {
      value: "FaShoppingCart",
      label: "Shopping Cart",
      icon: <FaShoppingCart />,
    },
    { value: "FaCalculator", label: "Calculator", icon: <FaCalculator /> },
    {
      value: "FaBalanceScale",
      label: "Balance Scale",
      icon: <FaBalanceScale />,
    },
    {
      value: "FaMoneyCheckAlt",
      label: "Money Check Alt",
      icon: <FaMoneyCheckAlt />,
    },
    {
      value: "IoGameController",
      label: "Gaming",
      icon: <IoGameController />,
    },
  ];
  const renderIcon = (iconName) => {
    const iconMapping = {
      IoGameController: <IoGameController />,
      FaCode: <FaCode />,
      FaLaptopCode: <FaLaptopCode />,
      FaMobileAlt: <FaMobileAlt />,
      FaServer: <FaServer />,
      FaDatabase: <FaDatabase />,
      FaCloud: <FaCloud />,
      FaRobot: <FaRobot />,
      FaMicrochip: <FaMicrochip />,
      FaWifi: <FaWifi />,
      FaSatelliteDish: <FaSatelliteDish />,
      FaGamepad: <FaGamepad />,
      FaChess: <FaChess />,
      FaDice: <FaDice />,
      FaPuzzlePiece: <FaPuzzlePiece />,
      FaPlaystation: <FaPlaystation />,
      FaXbox: <FaXbox />,
      FaSteam: <FaSteam />,
      FaTwitch: <FaTwitch />,
      FaTag: <FaTag />,
      FaTags: <FaTags />,
      FaBookmark: <FaBookmark />,
      FaBarcode: <FaBarcode />,
      FaBook: <FaBook />,
      FaCertificate: <FaCertificate />,
      FaClipboard: <FaClipboard />,
      FaFileAlt: <FaFileAlt />,
      FaFolderOpen: <FaFolderOpen />,
      FaMoneyBill: <FaMoneyBill />,
      FaMoneyCheck: <FaMoneyCheck />,
      FaChartLine: <FaChartLine />,
      FaChartBar: <FaChartBar />,
      FaWallet: <FaWallet />,
      FaShoppingCart: <FaShoppingCart />,
      FaBalanceScale: <FaBalanceScale />,
      FaCalculator: <FaCalculator />,
      FaMoneyCheckAlt: <FaMoneyCheckAlt />,
    };

    return iconMapping[iconName] || null;
  };

  return (
    <div className="form-group mb-3">
      <label className="form-label" htmlFor="icon">
        Icon
      </label>
      <Dropdown onSelect={handleSelectIcon}>
        <Dropdown.Toggle
          variant="success"
          id="dropdown-basic"
          style={{ width: "50%" }}
        >
          {icon ? renderIcon(icon) : "Select an Icon"}
        </Dropdown.Toggle>

        <Dropdown.Menu
          style={{
            maxHeight: "200px",
            overflow: "auto",
            marginLeft: "20px",
            width: "50%",
          }}
        >
          {iconOptions.map((opt) => (
            <Dropdown.Item
              className="d-flex align-items-center justify-content-between px-3"
              key={opt.value}
              eventKey={opt.value}
            >
              {opt.label}
              {opt.icon}
            </Dropdown.Item>
          ))}
        </Dropdown.Menu>
      </Dropdown>
    </div>
  );
};

SelectIcon.propTypes = {
  handleSelectIcon: PropTypes.func.isRequired,
  icon: PropTypes.string.isRequired,
  setIconError: PropTypes.func.isRequired,
};

export default SelectIcon;
