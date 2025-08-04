import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { sendResetPassowrdOTP } from "@/State/Auth/Action";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { z } from "zod";


const formSchema = z.object({
  email: z.string().email("Invalid email address"),
});

const ForgotPasswordForm = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [verificationType] = useState("EMAIL");


  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
    },
  });

  
  const onSubmit = (data) => {
    dispatch(
      sendResetPassowrdOTP({
        sendTo: data.email,
        navigate,
        verificationType,
      })
    );
    console.log("Form Submitted with:", data);
  };

  return (
    <div className="space-y-5">
      <h1 className="text-center text-xl font-semibold">
        Where do you want to get the code?
      </h1>

      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          {/* Email Field */}
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    type="email"
                    placeholder="Enter your email"
                    className="border w-full border-gray-700 py-5 px-5"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

      
          <Button type="submit" className="w-full bg-slate-400 py-5">
            Send OTP
          </Button>
        </form>
      </Form>
    </div>
  );
};

export default ForgotPasswordForm;
