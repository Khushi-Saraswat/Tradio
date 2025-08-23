import { Button } from '@/components/ui/button'
import { useLocation, useNavigate } from 'react-router-dom'
import 'react-toastify/dist/ReactToastify.css'
import './Authe.css'
import ForgotPassword from './ForgotPassword'
import SignIn from './SignIn'
import SignUp from './SignUp'

const Authe = () => {
  const location = useLocation()
  const navigate = useNavigate()

  // helper to navigate
  const handleNavigation = (path) => {
    navigate(path)
  }

  return (
    <div className="authContainer h-screen relative">
      <div
        className="bgBlure absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 
                   box flex flex-col justify-center items-center h-[35rem] w-[30rem] 
                   rounded-md z-50 bg-black bg-opacity-50 shadow-2xl shadow-white"
      >
        {location.pathname === '/signup' ? (
          <section className="w-full login">
            <div className="loginBox w-full px-10 space-y-5">
              <SignUp />
              <div className="flex items-center justify-center">
                <span>Already have an account?</span>
                <Button
                  onClick={() => handleNavigation('/signin')}
                  variant="ghost"
                >
                  Sign In
                </Button>
              </div>
            </div>
          </section>
        ) : location.pathname === '/forgot-password' ? (
          <ForgotPassword />
        ) : (
          <section className="w-full login">
            <div className="loginBox w-full px-10 space-y-5">
              <SignIn />

              <div className="flex items-center justify-center">
                <span>Don’t have an account?</span>
                <Button
                  onClick={() => handleNavigation('/signup')}
                  variant="ghost"
                >
                  Sign Up
                </Button>
              </div>
              <div>
                <Button
                  onClick={() => navigate('/forgot-password')}
                  variant="outline"
                  className="w-full py-5"
                >
                  Forgot Password?
                </Button>
              </div>
            </div>
          </section>
        )}
      </div>
    </div>
  )
}

export default Authe