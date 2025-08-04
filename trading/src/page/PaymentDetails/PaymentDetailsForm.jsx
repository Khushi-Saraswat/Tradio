import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { addPaymentDetails } from "@/State/Withdrawal/Action";
import { DialogClose } from "@radix-ui/react-dialog";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";

const PaymentDetailsForm = () => {

  const dispatch=useDispatch();


  const form = useForm({
    defaultValues: {
      accountHolderName: "",
      ifsc: "",
      accountNumber: "",
      bankName: "",
    },
  });

  const onSubmit = (data) => {
    dispatch(addPaymentDetails({
      paymentDetails:data,
      jwt:localStorage.getItem("jwt")
    }))
    console.log(data);
  };

  return (
    <div className="px-10 py-2">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
        <FormField
            control={form.control}
            name="accountHolderName"
            render={({ field }) => (
              <FormItem>
                <label>Account holder name</label>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="code with zosh"
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />

<FormField
            control={form.control}
            name="ifsc"
            render={({ field }) => (
              <FormItem>
                <label>IFSC Code</label>
                <FormControl>
                  <Input
                    {...field}
                    name="ifsc"
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="YESB0000009"
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />

            
          
<FormField
            control={form.control}
            name="accountNumber"
            type="password"
            render={({ field }) => (
              <FormItem>
                <label>Account Number</label>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="*********5602"
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />



<FormField
            control={form.control}
            name="confirmAccountNumber"
            render={({ field }) => (
              <FormItem>
                <label>Confirm Account Number</label>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="Confirm Account Number"
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />

        
<FormField
            control={form.control}
            name="bankName"
            render={({ field }) => (
              <FormItem>
                <label>Bank Name</label>
                <FormControl>
                  <Input
                    {...field}
                    className="border w-full border-gray-700 py-5 px-5"
                    placeholder="YES Bank"
                  />
                </FormControl>

                <FormMessage />
              </FormItem>
            )}
          />

          <DialogClose>
          <button type="submit" className="w-full py-5">
            Submit
          </button>
          </DialogClose>
          
        </form>
      </Form>
    </div>
  );
};

export default PaymentDetailsForm;
