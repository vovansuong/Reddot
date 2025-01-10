import axios from "../customize-axios";
export const getAllTags = async (
  page,
  size,
  orderBy,
  sort,
  search,
  accessToken,
  axiosJWT,
) => {
  try {
    const response = await axiosJWT.get("/admin/tags", {
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
    return response.data;
  } catch (error) {
    console.error("Error fetching discussions:", error);
    throw error; // Re-throw the error to handle it in the calling code if necessary
  }
};

export const createTag = async (tag, accessToken, axiosJWT) => {
  try {
    const res = await axiosJWT.post("/admin/tags/create", tag, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (err) {
    console.log(`Error createTag`, err);
    return;
  }
};

export const updateTag = async (tagToUpdate, accessToken, axiosJWT) => {
  try {
    const res = await axiosJWT.put("/admin/tags/update", tagToUpdate, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (err) {
    console.log(`Error updateTag`, err);
    return;
  }
};

export const viewAllTag = async (page, size, orderBy, sort, search) => {
  try {
    const response = await axios.get("/view/tags", {
      params: {
        page,
        size,
        orderBy,
        sort,
        search,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching discussions:", error);
    throw error;
  }
};
