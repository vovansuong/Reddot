import { fn } from '@storybook/test';
import FormInput from 'components/FormInput';

export default {
  title: 'Example/FormInput',
  component: FormInput,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {
    backgroundColor: { control: 'color' },
  },
  args: { onClick: fn() },
};

export const PasswordEmpty = {
      args: {
            id: "password",
      }
}