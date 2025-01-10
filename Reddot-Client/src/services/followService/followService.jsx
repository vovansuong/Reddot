//use in profilePage
export const postRegisterFollow = async (accessToken, axiosJWT, data) => {
  try {
    let res = await axiosJWT.post(`follows/register`, data, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    return res;
  } catch (err) {
    console.log(err);
  }
};

//use in profilePage
export const getFollowingByUserId = async (accessToken, axiosJWT, username) => {
  try {
    let res = await axiosJWT.get(`follows/following/${username}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    return res;
  } catch (err) {
    console.log(err);
  }
};

//use in profilePage
export const getFollowerByUserId = async (accessToken, axiosJWT, username) => {
  try {
    let res = await axiosJWT.get(`follows/follower/${username}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    return res;
  } catch (err) {
    console.log(err);
  }
};
