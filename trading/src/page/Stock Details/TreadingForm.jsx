import { Avatar } from '@/components/ui/avatar'
import { Input } from '@/components/ui/input'
import React, { useEffect, useState } from 'react'

import { Button } from '@/components/ui/button'
import { getAssetDetails } from '@/State/Assests/Action'
import { payOrder } from '@/State/Order/Action'
import { getUserWallet } from '@/State/Wallet/Action'
import { AvatarImage } from '@radix-ui/react-avatar'
import { DotIcon } from '@radix-ui/react-icons'
import { useDispatch, useSelector } from 'react-redux'


const TreadingForm = () => {

    const [orderType,setOrderType]=React.useState("BUY");
    const [amount,SetAmount]=useState(0)
    const[quantity,setQuantity]=useState(0)
    const {coin,wallet,asset,order}=useSelector((store) => (store))
    const dispatch=useDispatch()

    const handleChange = (e) =>{
        const amount=e.target.value;
        SetAmount(amount)
        const cryptoPrice = coin?.coinDetails?.market_data?.current_price?.usd || 60000;

        console.log(coin?.coinDetails?.market_data?.current_price?.usd)
        console.log(cryptoPrice+"cryptoPrice")
        const volume=calculateBuyCost(amount,cryptoPrice)
      
        console.log(volume)
        setQuantity(volume)
    }

    function calculateBuyCost(amountUSD, cryptoPrice) {
  if (!cryptoPrice || isNaN(cryptoPrice)) return 0;

  let volume = amountUSD / cryptoPrice;

  // Ensure cryptoPrice is a number before calling toString
  const priceString = cryptoPrice.toString();
  let decimalPlaces = Math.max(
    2,
    priceString.includes('.') ? priceString.split('.')[0].length : 2
  );

  return volume.toFixed(decimalPlaces);
}

   useEffect(() => {
   
    dispatch(getUserWallet(localStorage.getItem("jwt")));
    dispatch(getAssetDetails({coinId:coin?.coinDetails?.id,jwt:localStorage.getItem("jwt")}))

   },[])


   const handleBuyCrypto=() =>{
      console.log(coin?.coinDetails?.id+"coin id");
      dispatch(payOrder({jwt:localStorage.getItem("jwt"),
        amount,orderData:{coinId:coin?.coinDetails?.id,quantity,orderType}
        }))
   }


 
  return (
    <div className='space-y-10 p-5'>
       <div>
        <div className='flex gap-4 items-center justify-between'>
          <Input
           className='py-7 focus:outline-none'
           placeholder="Enter amount"
           onChange={handleChange}
           type="number"
           name="amount"
          />
          <div>
            <p className='border text-2xl flex justify-center items-center w-36 h-14 rounded-md'>{quantity}</p>
          </div>
        </div>
       {false &&<h1 className='text-red-600 text-center pt-4'>Insufficent wallet balance to buy</h1>


       }
       
       </div>
       <div className='flex gap-5 items-center'>
          <div>
          <Avatar>
            <AvatarImage
              src={"https://coin-images.coingecko.com/coins/images/1/large/bitcoin.png?1696501400"}
              />
             </Avatar>
          </div>

          <div>
            <div className='flex items-center gap-2'>
              <p>BTC</p>
              <DotIcon  className='text-gray-400'/>
              <p  className='text-gray-400'>Bitcoin</p>
            </div>
            <div className='flex items-end gap-2'>
               <p className='text-xl font-bold'>${coin?.coinDetails?.market_data?.current_price?.usd || 60000}</p>
               <p className='text-red-600'>
               <span>-1319049822.578</span>
                  <span>(-0.29803%)</span>
                
               </p>
            </div>
          </div>

         
        </div>
        <div className='flex items-center justify-between'>
                <p>Order Type</p>
                <p>Market Order</p>
          </div>

          <div className='flex item-center justify-between'>
             <p>{orderType == "BUY" ? "Available Cash" : "Available Quantity" }</p>
             <p>
            
             {orderType === "BUY"
              ? wallet?.userWallet?.balance ?? 400
              : asset?.assetDetails?.quantity ?? 0}

             </p>
          </div>

           <div>

                <Button 
                 onClick={handleBuyCrypto}
                
                className={`w-full py-6" ${orderType == "SELL" ? "bg-red-600 text-white" :" " }`}>
                     {
                        orderType
                     }

                </Button>
                <Button 
                     variant="link"
                    className="w-full mt-5 text-xl"
                
                onClick={()=>{setOrderType(orderType == "BUY" ? "SELL" : "BUY")}}>
                    {
                        orderType=="BUY" ? "or sell" : "or Buy"
                    }
                </Button>

           </div>

    </div>
  )
}

export default TreadingForm
