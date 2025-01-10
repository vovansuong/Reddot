import { Route, Routes } from "react-router-dom";
import NotFound from "../components/errorPage/NotFound";
import Layout from "../layouts/Layout";
import DashBoard from "../components/adminPage/adminDashBoard/DashBoardPage";
import UsersListManage from "../components/adminPage/userManage/UsersListManagePage";
import MemberProfile from "../components/memberProfilePage/MemberProfilePage";

import ForumManage from "../components/adminPage/forumManage/ForumManage";
import DiscussionManage from "../components/adminPage/discussionManage/DiscussionManage";
import DiscussionDetails from "../components/adminPage/discussionManage/DiscussionDetails";

import RequireAuth from "../components/authPage/RequireAuth";
import "../assets/scss/paper-dashboard.scss?v=1.3.0";

import EmailOption from "../components/adminPage/emailOptionManage/EmailOptionPage";
import TagsManage from "../components/adminPage/tagManage/TagsManage";
import BannedKeyword from "../components/adminPage/bannedKeyword/BannedKeyword";
import BadgeManage from "../components/adminPage/badgeManage/badgeManage";
import ConfigAvatar from "../components/adminPage/ConfigAvatar/ConfigAvatar";
import { ROLES } from "../constants/index";

function AdminRoutes() {
  return (
    <Routes>
      <Route exact path="/" element={<Layout route="routesAdmin" />}>
        <Route
          element={<RequireAuth allowedRoles={[ROLES.ADMIN, ROLES.MOD]} />}
        >
          <Route exact path="/" element={<DashBoard />} />
          <Route exact path="/dashboard" element={<DashBoard />} />
          <Route exact path="/tags" element={<TagsManage />} />
          <Route exact path="/banned-keywords" element={<BannedKeyword />} />

          <Route exact path="/discussions" element={<DiscussionManage />} />

          <Route
            exact
            path="/discussion/:discussionId"
            element={<DiscussionDetails />}
          />

          <Route element={<RequireAuth allowedRoles={[ROLES.ADMIN]} />}>
            <Route exact path="/forums" element={<ForumManage />} />
            <Route exact path="/users" element={<UsersListManage />} />
            <Route
              path="/member-profile/:username"
              element={<MemberProfile />}
            />
            <Route path="email-option" element={<EmailOption />} />
            <Route path="badges" element={<BadgeManage />} />
            <Route path="avatar-option" element={<ConfigAvatar />} />
          </Route>
        </Route>

        <Route exact path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
}

export default AdminRoutes;
