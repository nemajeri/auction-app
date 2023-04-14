import { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AppContext } from '../../utils/AppContextProvider';
import Form from '../../utils/forms/Form';
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
  // const { login } = useContext(AppContext);

  // const navigate = useNavigate();

  const handleLogin = (formData) => {
    console.log(formData);
  };

  return (
    <div className='wrapper login-page__wrapper'>
      <div className='login-page__headline'>
        <h3>LOGIN</h3>
      </div>
      <div className='login-page__content'>
        <Form
          fields={fields}
          submitText='LOGIN'
          onSubmit={handleLogin}
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
