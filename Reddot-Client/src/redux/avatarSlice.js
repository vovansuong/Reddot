import { createSlice } from "@reduxjs/toolkit";

const avatarSlice = createSlice({
  name: "avatar",
  initialState: {
    avatar: {
      name: null,
      isFetching: false,
      error: false,
    },
    msg: "",
  },
  reducers: {
    uploadAvatarStart: (state) => {
      state.avatar.isFetching = true;
      state.avatar.error = false;
    },
    uploadAvatarSuccess: (state, action) => {
      state.avatar.isFetching = false;
      state.avatar.name = action.payload;
    },
    uploadAvatarFailed: (state) => {
      state.avatar.isFetching = false;
      state.avatar.name = null;
      state.avatar.error = true;
    },
    clearAvatar: (state) => {
      state.avatar.name = null;
    },
  },
});

export const {
  uploadAvatarStart,
  uploadAvatarSuccess,
  uploadAvatarFailed,
  clearAvatar,
} = avatarSlice.actions;

export default avatarSlice.reducer;
