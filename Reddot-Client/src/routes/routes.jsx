import Home from "../components/homePage/Home";
import ForumGroup from "../components/forumsPage/ForumGroup";
import MemberList from "../components/memberPage/MemberListPage";

const routes = [
  {
    path: "/home",
    name: "home",
    icon: "fa-solid fa-house",
    component: <Home />,
    layout: "",
  },
  {
    path: "/forumGroup",
    name: "forums",
    icon: "fa-solid fa-list fa-xl",
    component: <ForumGroup />,
    layout: "",
  },
  {
    path: "/members",
    name: "members",
    icon: "fa-solid fa-users",
    component: <MemberList />,
    layout: "",
  },
];

export default routes;
