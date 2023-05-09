export const customStyles = {
  control: (provided, state) => ({
    ...provided,
    borderRadius: "0",
    paddingTop: 4,
    paddingBottom: 4,
    paddingLeft: 8,
    paddingRight: 8,
    border: state.isFocused ? '1px solid black' : '1px solid #d8d8d8',
    fontFamily: "Lato, sans-serif",
    fontWeight: "400",
    boxShadow: "none",
    "&:hover": {
      borderColor: "#d8d8d8",
    },
  }),
  placeholder: (provided) => ({
    ...provided,
    fontSize: "0.8rem",
    color: "#d8d8d8"
  }),
  option: (provided, state) => ({
    ...provided,
    color: state.isSelected ? "white" : "black",
    backgroundColor: state.isSelected ? "#8367d8" : "white",
    "&:hover": {
      backgroundColor: "#8367d8",
      color: "white",
    },
    fontFamily: "Lato, sans-serif",
    fontWeight: "400",
  }),
  menu: (provided) => ({
    ...provided,
    borderRadius: "0",
  }),
};