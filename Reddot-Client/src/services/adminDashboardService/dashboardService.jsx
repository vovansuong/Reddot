export const getTotalData = async (accessToken, axiosJWT) => {
  return await axiosJWT.get(`admin/dashboard/total-data`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const getDataChart = async (accessToken, axiosJWT) => {
  try {
    return await axiosJWT.get(`admin/dashboard/chart-by-forum`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
  } catch (err) {
    console.log(err);
  }
};
