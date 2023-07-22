import {
    FIELD_NAME,
    FIELD_PLACEHOLDER,
    SORT_OPTIONS,
  } from '../utils/constants';

const sortingField = {
    name: FIELD_NAME,
    placeholder: FIELD_PLACEHOLDER,
    options: [
      { label: 'Default sorting', value: SORT_OPTIONS.DEFAULT_SORTING },
      { label: 'Sort By Newness', value: SORT_OPTIONS.START_DATE },
      { label: 'Sort By Time Left', value: SORT_OPTIONS.END_DATE },
      {
        label: 'Sort By Price: Low to High',
        value: SORT_OPTIONS.PRICE_LOW_TO_HIGH,
      },
      {
        label: 'Sort By Price: High to Low',
        value: SORT_OPTIONS.PRICE_HIGH_TO_LOW,
      },
    ],
}

export default sortingField;