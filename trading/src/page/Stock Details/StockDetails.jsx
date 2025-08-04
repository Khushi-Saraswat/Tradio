import { Avatar } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "@/components/ui/dialog"

import { fetchCoinsDetails } from '@/State/Coin/Action'
import { addItemToWatchlist, getUserWatchlist } from '@/State/WatchList/Action'
import { existInWatchlist } from '@/utils/existInWatchList'
import { AvatarImage } from '@radix-ui/react-avatar'
import { BookmarkFilledIcon, BookmarkIcon, DotIcon } from '@radix-ui/react-icons'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useParams } from 'react-router-dom'
import StockChart from '../Home/StockChart'
import TreadingForm from './TreadingForm'



const StockDetails = () => {

    
  const dispatch=useDispatch()
   const {id}=useParams()

   const { coin ,watchlist} = useSelector(store => store);

   useEffect(()=>{
        dispatch(fetchCoinsDetails({coinId:id,jwt:localStorage.getItem("jwt")}))
        dispatch(getUserWatchlist({jwt:localStorage.getItem("jwt")}))
   },[id])
    console.log("coin",coin)

    console.log(coin.coinDetails?.id,"coinid");

    const handleAddToWatchlist = () => {
       dispatch(addItemToWatchlist({coinId:coin.coinDetails?.id,jwt:localStorage.getItem("jwt")}))

    }





  

   return (
    <div className='p-5 mt-5'>
      <div className='flex justify-between'>
        <div className='flex gap-5 items-center'>
          <div>
          <Avatar>
            <AvatarImage
              src={coin.coinDetails?.image.large}
              />
             </Avatar>
          </div>

          <div>
            <div className='flex items-center gap-2'>
              <p>{coin.coinDetails?.symbol.toUpperCase()}</p>
              <DotIcon  className='text-gray-400'/>
              <p  className='text-gray-400'>{coin.coinDetails?.name}</p>
            </div>
            <div className='flex items-end gap-2'>
               <p className='text-xl font-bold'>$
                {
                  coin.coinDetails?.market_data.current_price.usd
                }
               </p>
               <p className='text-red-600'>
               <span>-{ coin.coinDetails?.market_data.market_cap_change_24h}</span>
                  <span> (-{coin.coinDetails?.market_data_cap_change_percentage_24h}%)</span>
                
               </p>
            </div>
          </div>




        </div>
        <div className='flex items-center gap-5'>
          <Button onClick={handleAddToWatchlist}>
            {
              existInWatchlist(watchlist.items,coin.coinDetails?.id)? (
                   <BookmarkFilledIcon className='h-6 w-6' />
              ) : (
                   <BookmarkIcon className='h-6 w-6'/>
              )}
            
          </Button>
          <Dialog>
          <DialogTrigger>
            <Button size="lg">
              Tread
            </Button>
          </DialogTrigger>
          <DialogContent>
          <DialogHeader>
          <DialogTitle>How much you want to spend?</DialogTitle>
          </DialogHeader>
          <TreadingForm/>
         </DialogContent>
         </Dialog>

        </div>

        
        </div>
          <div className='mt-14'>
          <StockChart  coinId={id}/>
          </div>
      
    </div>
  )
}

export default StockDetails
