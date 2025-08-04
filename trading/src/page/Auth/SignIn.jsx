import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { login } from "@/State/Auth/Action";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { z } from "zod";

const formSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(8, "Password must be at least 8 characters long"),
});

const SignIn = () => {

  const dispatch=useDispatch();
  const navigate=useNavigate();
   const {auth}=useSelector(store=>store);

 console.log("auth",auth.loading);

  const form = useForm({
      resolver: zodResolver(formSchema),
    defaultValues: {
     
      email: "",
      password: "",
      
    },
  });

  const onSubmit = (data) => {
    console.log(data);
    dispatch(login({data,navigate}));
  };

  return (
    <div>
      
      <h1 className="text-xl font-bold text-center pb-3">Login</h1>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
         
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
            
         
       

         
           
           <Button type="submit" className="w-full  py-5">
              Login
            </Button>
          
           
          

         
          
        </form>
      </Form>
    </div>
  );
};

export default SignIn;
