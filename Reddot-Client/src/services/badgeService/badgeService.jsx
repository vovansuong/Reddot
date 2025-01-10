//use in badge manage page

export const getAllBadge = async (accessToken, axiosJWT) => {
  try {
    return await axiosJWT.get("admin/badges", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
  } catch (err) {
    console.log(err);
  }
};

//update badge
export const putUpdateBadge = async (badge, accessToken, axiosJWT) => {
  try {
    return await axiosJWT.put(`admin/badges/update/${badge.id}`, badge, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
  } catch (err) {
    console.log(err);
  }
};

//set badge for all user
export const setBadgeForAllUser = async (accessToken, axiosJWT) => {
  try {
    return await axiosJWT.get("admin/badges/set-all-user", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
  } catch (err) {
    console.log(err?.message);
  }
};
