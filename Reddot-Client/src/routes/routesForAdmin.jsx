import DashBoard from "../components/adminPage/adminDashBoard/DashBoardPage";
import DiscussionManage from "../components/adminPage/discussionManage/DiscussionManage";
import ForumManage from "../components/adminPage/forumManage/ForumManage";
import TagsStat from "../components/adminPage/tagManage/TagsManage";
import TableUsers from "../components/adminPage/userManage/UsersListManagePage";

import EmailOption from "../components/adminPage/emailOptionManage/EmailOptionPage";
import BadgeManage from "../components/adminPage/badgeManage/badgeManage";
import ConfigAvatar from "../components/adminPage/ConfigAvatar/ConfigAvatar";
import { ROLES } from "../constants/";

import BannedKeyword from "../components/adminPage/bannedKeyword/BannedKeyword";

const routes = [
  {
    path: "/dashboard",
    name: "dashboard",
    icon: "fa-solid fa-building-columns",
    component: <DashBoard />,
    layout: "/admin",
    roles: [ROLES.ADMIN, ROLES.MOD],
  },
  {
    path: "/users",
    name: "users manage",
    icon: "fa-solid fa-people-group",
    component: <TableUsers />,
    layout: "/admin",
    roles: [ROLES.ADMIN],
  },
  {
    path: "/forums",
    name: "forums manage",
    icon: "fa-solid fa-users",
    component: <ForumManage />,
    layout: "/admin",
    roles: [ROLES.ADMIN],
  },
  {
    path: "/discussions",
    name: "discussions manage",
    icon: "fa-solid fa-comments",
    component: <DiscussionManage />,
    layout: "/admin",
    roles: [ROLES.ADMIN, ROLES.MOD],
  },
  {
    path: "/tags",
    name: "tags manage",
    icon: "fa-solid fa-tags",
    component: <TagsStat />,
    layout: "/admin",
    roles: [ROLES.ADMIN, ROLES.MOD],
  },
  {
    path: "/banned-keywords",
    name: "banned keywords",
    icon: "fa-solid fa-file-word",
    component: <BannedKeyword />,
    layout: "/admin",
  },
  {
    path: "/badges",
    name: "Badge manage",
    icon: "fa-solid fa-shield-cat",
    component: <BadgeManage />,
    layout: "/admin",
    roles: [ROLES.ADMIN],
  },
  {
    path: "/email-option",
    name: "Config email",
    icon: "fa-solid fa-envelope",
    component: <EmailOption />,
    layout: "/admin",
    roles: [ROLES.ADMIN],
  },
  {
    path: "/avatar-option",
    name: "Config Avatar Option",
    icon: "fa-solid fa-gear",
    component: <ConfigAvatar />,
    layout: "/admin",
    roles: [ROLES.ADMIN],
  },
];

export default routes;
