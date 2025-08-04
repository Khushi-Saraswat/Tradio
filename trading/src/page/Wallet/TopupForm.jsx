import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { paymentHandler } from '@/State/Wallet/Action';
import { Label } from '@radix-ui/react-label';
import { RadioGroup, RadioGroupItem } from '@radix-ui/react-radio-group';
import React from 'react';
import { useDispatch } from 'react-redux';

const TopupForm = () => {
  const dispatch = useDispatch();

  const [amount, setAmount] = React.useState('');
  const [paymentMethod, setPaymentMethod] = React.useState('RAZORPAY');

  const handleChange = (e) => {
    setAmount(e.target.value);
  };

  const handlePaymentMethodChange = (value) => {
    setPaymentMethod(value);
  };

  const handleSubmit = () => {
    console.log(amount, paymentMethod);
    dispatch(paymentHandler({ jwt: localStorage.getItem('jwt'), amount, paymentMethod }));
  };

  return (
    <div className="pt-10 space-y-5">
      <div>
        <h1 className="pb-1">Enter Amount</h1>
        <Input
          onChange={handleChange}
          value={amount}
          className="py-7 text-lg"
          placeholder="$9999"
        />
      </div>

      <div>
        
        <RadioGroup
          className="flex space-x-5"
          value={paymentMethod}
          onValueChange={handlePaymentMethodChange}
        >
          {/* Razorpay Option */}
          <div
            className={`flex items-center space-x-3 border p-3 px-5 rounded-md transition-all duration-200 ${
              paymentMethod === 'RAZORPAY' ? 'border-blue-500 bg-blue-50' : 'border-gray-300'
            }`}
          >
            <RadioGroupItem
              value="RAZORPAY"
              id="r1"
              className="h-5 w-5 rounded-full border border-gray-400 data-[state=checked]:bg-blue-500 data-[state=checked]:border-blue-500"
            />
            <Label htmlFor="r1" className="cursor-pointer">
              <div className="bg-white rounded-md px-5 py-2 w-32">
                <img src="/razorpay.png" className="h-10 mx-auto" alt="Razorpay" />
              </div>
            </Label>
          </div>
        </RadioGroup>
      </div>

      <Button onClick={handleSubmit} className="w-full py-7">
        Submit
      </Button>
    </div>
  );
};

export default TopupForm;
