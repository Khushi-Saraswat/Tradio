import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { register } from "@/State/Auth/Action";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { z } from "zod";
const SignUp = () => {
  

 const formSchema = z.object({
  fullName: z.string().nonempty("Full name is required"),
  email: z.string().email("Invalid email address").optional(),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters long")
    .optional(),
    mobile: z.string().min(10, "Mobile number is required")
});



  const navigate=useNavigate();
  const dispatch=useDispatch();
  const {auth}=useSelector(store=>store);
  console.log("auth",auth);
  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      fullName: "",
      email: "",
      password: "",
      mobile:""
      
      
    },
  });


  const onSubmit = (data) => {
      console.log(data+"data in signup");
    dispatch(register({data,navigate}));

  };

  return (
    <div>
      <h1 className="text-xl font-bold text-ce pb-3">Register</h1>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
        <FormField
         control={form.control}
        name="fullName" 
        render={({ field }) => (
        <FormItem>
        <FormControl>
        <Input
          className="border w-full border-gray-700 p-5"
          placeholder="code with zosh"
          {...field}
        />
        </FormControl>
        <FormMessage />
        </FormItem>
  )}
/>

            <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
              
                <FormControl>
                  <Input
                    
                    className="border w-full border-gray-700 p-5"
                    placeholder="codewithzosh@gmail.com"
                    {...field}
                  />
                </FormControl>
                
                <FormMessage />
              </FormItem>
            )}

            
          />
         <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
              
                <FormControl>
                  <Input
                    className="border w-full border-gray-700 p-5"
                    placeholder="your password"
                    {...field}
                  />
                </FormControl>
                
                <FormMessage />
              </FormItem>
            )}
          />
          
            
             <FormField
            control={form.control}
            name="mobile"
            render={({ field }) => (
              <FormItem>
              
                <FormControl>
                  <Input
                    className="border w-full border-gray-700 p-5"
                    placeholder="your mobile"
                    {...field}
                  />
                </FormControl>
                
                <FormMessage />
              </FormItem>
            )}
          />
          
            


            <Button type="submit" className="w-full  py-5">
              Register
            </Button>
          
        </form>
      </Form>
    </div>
  );
};

export default SignUp;
