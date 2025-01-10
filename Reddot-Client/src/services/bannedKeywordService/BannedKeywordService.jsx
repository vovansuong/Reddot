const getAllBannedKeywords = async (accessToken, axiosJWT) => {
  try {
    let res = await axiosJWT.get("/banned-keywords/all", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res.data;
  } catch (err) {
    console.log(err);
  }
};

const getPageBannedKeywords = async (
  page,
  size,
  orderBy,
  sort,
  search,
  accessToken,
  axiosJWT,
) => {
  try {
    const res = await axiosJWT.get("/banned-keywords/paginate", {
      params: {
        page,
        size,
        orderBy,
        sort,
        search,
      },
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (error) {
    console.error("Error fetching discussions:", error);
    throw error;
  }
};

const createBannedKeyword = async (data, accessToken, axiosJWT) => {
  try {
    let res = await axiosJWT.post("/banned-keywords/create", data, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (err) {
    console.log(err);
  }
};

const updateBannedKeyword = async (id, keyword, accessToken, axiosJWT) => {
  try {
    let res = await axiosJWT.put(`/banned-keywords/update/${id}`, keyword, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res.data;
  } catch (err) {
    console.log(err);
  }
};

const deleteBannedKeyword = async (id, accessToken, axiosJWT) => {
  try {
    let res = await axiosJWT.delete(`/banned-keywords/delete/${id}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res.data;
  } catch (err) {
    console.log(err);
  }
};

export {
  getAllBannedKeywords,
  getPageBannedKeywords,
  createBannedKeyword,
  updateBannedKeyword,
  deleteBannedKeyword,
};
