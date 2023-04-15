import { Link } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginUser } from '../../utils/api/authApi';
import './loginPage.css';

const fields = [
  {
    name: 'email',
    label: 'Email',
    type: 'email',
  },
  {
    name: 'password',
    label: 'Password',
    type: 'password',
  },
];

const LoginPage = () => {

  return (
    <div className='wrapper login-page__wrapper'>
      <div className='login-page__headline'>
        <h3>LOGIN</h3>
      </div>
      <div className='login-page__content'>
        <Form
          fields={fields}
          submitText='LOGIN'
          onSubmit={loginUser}
          includeSocial={true}
          includeRememberMe={true}
        />
        <div className='login-page__password'>
          <Link to={'/password'}>Forgot password?</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
