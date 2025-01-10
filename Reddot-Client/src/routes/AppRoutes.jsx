import { Route, Routes } from "react-router-dom";
import NotFound from "../components/errorPage/NotFound";
import Home from "../components/homePage/Home";
import ForumGroup from "../components/forumsPage/ForumGroup";
import LoginForm from "../components/authPage/LoginForm";
import RegisterForm from "../components/authPage/RegisterForm";
import OAuth2RedirectHandler from "../components/authPage/oauth2/OAuth2RedirectHandler";
import EmailConfirm from "../components/authPage/EmailConfirmPage";

import Discussion from "../components/discussions/Discussions";
import DiscussionDetails from "../components/discussions/DiscussionDetails";
import ListDiscussions from "../components/discussions/ListDiscussions";

import MemberList from "../components/memberPage/MemberListPage";
import MemberProfile from "../components/memberProfilePage/MemberProfilePage";
import ChangePassword from "../components/memberProfilePage/elements/ChangePasswordPage";
import Layout from "../layouts/Layout";
import ForgotPassword from "../components/authPage/resetPassword/ForgotPassword";
import UpdatePassword from "../components/authPage/resetPassword/UpdatePassword";
import Unauthorized from "../components/errorPage/Unauthorized";
import RequireAuth from "../components/authPage/RequireAuth";
import TermsAndConditions from "../components/otherPage/TermsAndConditions";
import PrivacyPolicy from "../components/otherPage/PrivacyPolicy";
import RedirectHandlerAfterLogin from "../components/authPage/RedirectHandlerAfterLogin";

const ROLES = {
  ADMIN: "ROLE_ADMIN",
  USER: "ROLE_USER",
  MOD: "ROLE_MOD",
};

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginForm />} />
      <Route path="/register" element={<RegisterForm />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path="/reset-password" element={<UpdatePassword />} />

      <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />

      <Route path="/email-confirm" element={<EmailConfirm />} />

      <Route path="/" element={<Layout route="routes1" />}>
        {/* public route */}
        <Route path="unauthorized" element={<Unauthorized />} />

        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/term" element={<TermsAndConditions />} />
        <Route path="/policy" element={<PrivacyPolicy />} />

        <Route path="/forumGroup" element={<ForumGroup />} />

        <Route path="/forum/:forumId" element={<Discussion />} />
        <Route
          path="/discussion/:discussionId"
          element={<DiscussionDetails />}
        />

        <Route path="/list-discussion" element={<ListDiscussions />} />

        <Route path="/members" element={<MemberList />} />

        <Route
          element={
            <RequireAuth allowedRoles={[ROLES.ADMIN, ROLES.MOD, ROLES.USER]} />
          }
        >
          <Route path="/redirect-to" element={<RedirectHandlerAfterLogin />} />
          <Route path="/member-profile/:username" element={<MemberProfile />} />
          <Route
            path="/change-password/:username"
            element={<ChangePassword />}
          />
        </Route>

        {/* catch all */}
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
}

export default AppRoutes;
