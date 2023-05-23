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

export const headerClassNames = [
  'auction-table__first-header space-left',
  'auction-table__second-header space-right space-left-second__header',
  'auction-table__third-header',
  'auction-table__fourth-header',
  'auction-table__fifth-header',
  'auction-table__sixth-header space-right',
];

export const bodyClassNames = [
  'auction-table__first-body space-left',
  'auction-table__second-body',
  'auction-table__third-body',
  'auction-table__fourth-body',
  'auction-table__fifth-body',
  'auction-table__sixth-body',
];

export const landingPageProductClassName = 'landing-page-product';