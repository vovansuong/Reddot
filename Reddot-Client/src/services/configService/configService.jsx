export const getAvatarOption = async (axiosJWT, accessToken) => {
  try {
    const res = await axiosJWT.get(`admin/avatar-options`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (error) {
    console.log(`Error: ${error}`);
    return null;
  }
};

export const updateAvatarOption = async (data, axiosJWT, accessToken) => {
  try {
    const res = await axiosJWT.post(`admin/avatar-options/update`, data, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (error) {
    console.log(`Error: ${error}`);
    return null;
  }
};
