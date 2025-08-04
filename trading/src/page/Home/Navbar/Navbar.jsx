

import { Button } from '@/components/ui/button'
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "@/components/ui/sheet"

import { Avatar, AvatarFallback, AvatarImage } from '@radix-ui/react-avatar'
import { DragHandleHorizontalIcon, MagnifyingGlassIcon } from '@radix-ui/react-icons'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import Sidebar from './Sidebar'


  
const Navbar = () => {

  const {auth}=useSelector(store=>store)
  const navigate = useNavigate();
  const handleNavigate=()=>{
    if (auth.user) {
    console.log("User object:", auth.user); // <-- Add this
    if (auth.user.role === "ROLE_ADMIN") {
      navigate("/admin/withdrawal");
    } else {
      navigate("/profile");
    }
  }
  }

  return (
   <div className="px-2 py-3 border-b z-50 bg-background bg-opacity-0 sticky top-0 left-0 right-0 flex justify-between items-center">
        <div className="flex items-center gap-3">
          <Sheet className="">
            <SheetTrigger>
              <Button
                className="rounded-full h-11 w-11"
                variant="ghost"
                size="icon"
              >
                <DragHandleHorizontalIcon className=" h-7 w-7" />
              </Button>
            </SheetTrigger>
            <SheetContent
              className="w-72  border-r-0 flexs flex-col  justify-center"
              side="left"
            >
              <SheetHeader>
                <SheetTitle>
                  <div className="text-3xl flex justify-center  items-center gap-1">
                    <Avatar className="h-10 w-10">
                     
                      <AvatarImage
  src="https://cdn.pixabay.com/photo/2021/04/30/16/47/binance-logo-6219389_1280.png"
  alt="Tradio Logo"
  className="w-8 h-8 lg:w-10 lg:h-10 rounded-full object-cover shadow-md border border-gray-300 transition-transform duration-300 hover:scale-105"
/>

                    </Avatar>
                    <div className=' cursor-pointer transition-all duration-300 hover:scale-105 hover:text-blue-600'>
                      <span className="font-bold text-orange-700">Tra</span>
                      <span>dio</span>
                    </div>
                  </div>
                </SheetTitle>
              </SheetHeader>
              <Sidebar />
            </SheetContent>
          </Sheet>

          <p
            onClick={() => navigate("/")}
            className="text-sm lg:text-base font-semibold text-primary cursor-pointer transition-all duration-300 hover:scale-105 hover:text-blue-600"
          >
            Tradio
          </p>
          <div className="p-0 ml-9">
            <Button
              variant="outline"
              onClick={() => navigate("/search")}
              className="flex items-center gap-3"
            >
              {" "}
              <MagnifyingGlassIcon className="left-2 top-3 " />
              <span>Search</span>
            </Button>
          </div>
        </div>
        <div>
                   <Avatar className="cursor-pointer" onClick={handleNavigate}>
            {!auth.user ? (
              <AvatarIcon className=" h-8 w-8" />
            ) : (
              <AvatarFallback>  {auth.user?.fullName ? auth.user.fullName[0].toUpperCase() : "U"}</AvatarFallback>
            )}
          </Avatar>
        </div>
      </div>
  )
}

export default Navbar
