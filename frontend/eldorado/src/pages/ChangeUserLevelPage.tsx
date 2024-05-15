"use client"

import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"

import {Button} from "@/components/ui/button"
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage,} from "@/components/ui/form"
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group"
import {toast} from "@/components/ui/use-toast"
import SiteHeader from "@/components/SiteHeader.tsx";
import {Card, CardHeader} from "@/components/ui/card.tsx";
import {useAccountState} from "@/context/AccountContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {useAccount} from "@/hooks/useAccount.ts";

const FormSchema = z.object({
    type: z.any()
})

function ChangeUserLevelPage() {
    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
    })
    const navigator = useNavigate()
    const {account} = useAccountState()

    const {getCurrentAccount} = useAccount();

    //It has to be that way afaik so ignore the warning and enjoy your day :)
    useEffect(() => {
        getCurrentAccount();
    }, []);


    function onSubmit(data: z.infer<typeof FormSchema>) {
        try {
            if (account) {
                account.activeUserLevel = data.type;
                localStorage.setItem('account', JSON.stringify(account));
            }
            toast({
                title: "Hurray!",
                description: "User level successfully changed."
            })
            navigator("/home")

        } catch (e) {
            toast({
                title: "Oh no!",
                description: "Something went wrong."
            })
        }
        toast({
            title: "Hurray!",
            description: "User level successfully changed!"
        })
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <div className="flex justify-between items-center mx-auto p-10">
                <Card>
                    <CardHeader className={"p-3 font-bold"}>
                        Select your active role:
                    </CardHeader>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className=" space-y-5 p-5">
                            <FormField
                                control={form.control}
                                name="type"
                                render={({field}) => (
                                    <FormItem className="space-y-3">
                                        <FormControl>
                                            <RadioGroup
                                                onValueChange={field.onChange}
                                                defaultValue={account?.activeUserLevel.roleName}
                                                className="flex flex-col space-y-1"
                                            >
                                                {account?.userLevelsDto.map((userLevel, index) => (
                                                    <FormItem key={index}
                                                              className="flex items-center space-x-3 space-y-0">
                                                        <FormControl>
                                                            <RadioGroupItem value={userLevel.roleName}/>
                                                        </FormControl>
                                                        <FormLabel className="font-normal">
                                                            {userLevel.roleName}
                                                        </FormLabel>
                                                    </FormItem>
                                                ))}
                                            </RadioGroup>
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                            <Button type="submit">Save</Button>
                        </form>
                    </Form>
                </Card>
            </div>
        </div>
    )
}

export default ChangeUserLevelPage
