export const getEmailOptionById = async (accessToken, axiosJWT) => {
  return await axiosJWT.get(`admin/email-manage`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const updateEmailOption = async (emailOption, accessToken, axiosJWT) => {
  return await axiosJWT.post(`admin/email-manage/update`, emailOption, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const sendEmail = async (emailOption, accessToken, axiosJWT) => {
  return await axiosJWT.post(`admin/email-manage/send-email`, emailOption, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};

export const getAllEmail = async (accessToken, axiosJWT) => {
  return await axiosJWT.get("admin/email-manage/all", {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
};
