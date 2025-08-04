import CustomToast from '@/components/custome/CustomToast'
import { Button } from '@/components/ui/button'
import { useToast } from '@/components/ui/use-toast'
import { useState } from 'react'
import { useSelector } from 'react-redux'
import { useLocation, useNavigate } from 'react-router-dom'
import 'react-toastify/dist/ReactToastify.css'
import './Authe.css'
import ForgotPassword from './ForgotPassword'
import SignIn from './SignIn'
import SignUp from './SignUp'
const Authe = () => {

  const navigate = useNavigate();
  const location = useLocation();
  const { auth } = useSelector((store) => store);
  const {toast}=useToast()
  const [animate, setAnimate] = useState(false);

  const handleNavigation = (path) => {
    // setAnimate(true);
    // setTimeout(() => {
    navigate(path);
    console.log(path+"path in handle navigation");
    //   setAnimate(false);
    // }, 500);
    // Adjust the delay as needed to match your animation duration
    // setAnimate(false)
  };

  const [showToast, setShowToast] = useState(false);

  const handleShowToast = () => {
    setShowToast(true);
  };

   console.log("---------- ",auth.error)


  return (
    <div className={`authContainer h-screen relative`}>
      <div className="absolute top-0 right-0 left-0 bottom-0 bg-[#030712] bg-opacity-50"></div>

      <div
        className={`bgBlure absolute top-1/2 left-1/2 transform -translate-x-1/2  -translate-y-1/2 box flex flex-col justify-center items-center  h-[35rem] w-[30rem]  rounded-md z-50 bg-black bg-opacity-50 shadow-2xl shadow-white`}
      >
           <CustomToast show={auth.error} message={auth.error?.error}/> 
     

        <h1 className="text-6xl font-bold pb-9">Zosh Trading</h1>


        {location.pathname == "/signup" ? (
          <section
            className={`w-full login  ${animate ? "slide-down" : "slide-up"}`}
          >
            <div className={`  loginBox  w-full px-10 space-y-5 `}>
              <SignUp />

              {location.pathname == "/signup" ? (
                <div className="flex items-center justify-center">
                  <span> {"don't have account ?"} </span>
                  <Button
                    onClick={() => handleNavigation("/signin")}
                    variant="ghost"
                  >
                    signin
                  </Button>
                </div>
              ) : (
                <div className="flex items-center justify-center">
                  <span>already have account ? </span>
                  <Button
                    onClick={() => handleNavigation("/signup")}
                    variant="ghost"
                  >
                    signup
                  </Button>
                </div>
              )}
            </div>
          </section>
        ) : location.pathname == "/forgot-password" ? (
          <section className="p-5 w-full">
            <ForgotPassword />
            {/* <Button variant="outline" className="w-full py-5 mt-5">
              Try Using Mobile Number
            </Button> */}
            <div className="flex items-center justify-center mt-5">
              <span>Back To Login ? </span>
              <Button onClick={() => navigate("/signin")} variant="ghost">
                signin
              </Button>
            </div>
          </section>
        ) : (
          <>
            {
              <section className={`w-full login`}>
                <div className={`  loginBox  w-full px-10 space-y-5 `}>
                  <SignIn/>

                  <div className="flex items-center justify-center">
                    <span>already have account ? </span>
                    <Button
                      onClick={() => handleNavigation("/signup")}
                      variant="ghost"
                    >
                      signup
                    </Button>
                  </div>
                  <div className="">
                    <Button
                      onClick={() => navigate("/forgot-password")}
                      variant="outline"
                      className="w-full py-5"
                    >
                      Forgot Password ?
                    </Button>
                  </div>
                </div>
              </section>
            }
          </>
        )}


      </div>
      
    

    </div>

      );
};
 
  

export default Authe
