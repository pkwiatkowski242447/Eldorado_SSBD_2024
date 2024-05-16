import {useEffect, useState} from 'react';
import SiteHeader from "@/components/SiteHeader.tsx";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useForm} from "react-hook-form";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {isValidPhoneNumber} from "react-phone-number-input/min";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {useAccountState} from "@/context/AccountContext.tsx";
import {parsePhoneNumber} from "react-phone-number-input";
import {api} from "@/api/api.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccount} from "@/hooks/useAccount.ts";

const emailSchema = z.object({
    email: z.string().email({message: "New email is required."}),
});

const userDataSchema = z.object({
    name: z.string().min(2, {message: "First name has to be at least 2 characters long."})
        .max(32, {message: "First name has to be at most 32 characters long."}),
    lastName: z.string().min(2, {message: "Last name has to be at least 2 characters long."})
        .max(32, {message: "Last name has to be at most 32 characters long."}),
    phoneNumber: z.string().refine(isValidPhoneNumber, {message: "Invalid phone number"}),
});

function AccountSettings() {
    const [activeForm, setActiveForm] = useState('Authentication');

    const {account} = useAccountState();

    const phoneNumber = parsePhoneNumber(account?.phone || '')
    const e164Number = phoneNumber?.format('E.164')

    const {getCurrentAccount} = useAccount();

    //It has to be that way afaik so ignore the warning and enjoy your day :)
    useEffect(() => {
        getCurrentAccount();
    }, []);


    const formEmail = useForm({
        resolver: zodResolver(emailSchema),
        // defaultValues: {
        //     email: account?.email || '',
        // },
    });

    const formUserData = useForm({
        resolver: zodResolver(userDataSchema),
        // defaultValues: {
        //     name: account?.name || '',
        //     lastName: account?.lastname || '',
        //     phoneNumber: account?.phone || '',
        // },
    });

    const onSubmitEmail = (values: z.infer<typeof emailSchema>) => {
        api.changeEmailSelf(values.email).then(() => {
            getCurrentAccount();

            toast({
                title: "Success!",
                description: "Your email has been successfully changed.",
            });
            setTimeout(() => {
                window.location.reload()
            }, 3000);

        }).catch((error) => {
            toast({
                variant: "destructive",
                description: "Something went wrong. Please try again later.",
            })
            console.log(error.response.data)
        });
    };

    const onSubmitUserData = (values: z.infer<typeof userDataSchema>) => {
        const etag = window.localStorage.getItem('etag');
        if (account && account.accountLanguage && etag !== null) {
            api.modifyAccountSelf(account.login, account.version, account.userLevelsDto,
                values.name, values.lastName, values.phoneNumber, false, etag)
                .then(() => {
                    getCurrentAccount();
                    window.location.reload()
                    toast({
                        title: "Success!",
                        description: "Your account info has been successfully changed.",
                    });
                }).catch((error) => {
                    toast({
                        variant: "destructive",
                        description: "Something went wrong. Please try again later.",
                    })
                    console.log(error.response.data)
                }
            );
        } else {
            console.log('Account or account language is not defined');
        }
    };

    return (
        <div>
            <SiteHeader/>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">Modify Your Account</h1>
                </div>
                <div
                    className="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
                    <nav
                        className="grid gap-4 text-sm text-muted-foreground"
                    >
                        <a href="#" className="font-semibold text-primary"
                           onClick={() => setActiveForm('Authentication')}>
                            Authentication
                        </a>
                        <a href="#" onClick={() => setActiveForm('Personal Info')}>Personal Info</a>

                    </nav>
                    <div className="grid gap-6">
                        {activeForm === 'Authentication' && (
                            <div>
                                <Card className="mx-10 w-auto">
                                    <CardContent>
                                        <Form {...formEmail}>
                                            {// @ts-expect-error - fix this
                                                <form onSubmit={formEmail.handleSubmit(onSubmitEmail)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-5">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formEmail.control}
                                                                name="email"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel className="text-black">New
                                                                            E-Mail</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.email} {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}/>
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2">
                                                            Change your email
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                {/*<Card className="mx-10 w-auto">*/}
                                {/*    <CardContent>*/}
                                {/*        <Form {...form}>*/}
                                {/*            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">*/}
                                {/*                <div className="grid gap-4 p-5">*/}
                                {/*                    <div className="grid gap-2">*/}
                                {/*                        <FormField*/}
                                {/*                            control={form.control}*/}
                                {/*                            name="newPassword"*/}
                                {/*                            render={({field}) => (*/}
                                {/*                                <FormItem>*/}
                                {/*                                    <FormLabel className="text-black">New Password</FormLabel>*/}
                                {/*                                    <FormControl>*/}
                                {/*                                        <Input placeholder="" {...field} />*/}
                                {/*                                    </FormControl>*/}
                                {/*                                    <FormMessage/>*/}
                                {/*                                </FormItem>*/}
                                {/*                            )}/>*/}
                                {/*                    </div>*/}
                                {/*                    <Button type="submit" className="w-full pb-2">*/}
                                {/*                        Change your password*/}
                                {/*                    </Button>*/}
                                {/*                </div>*/}
                                {/*            </form>*/}
                                {/*        </Form>*/}
                                {/*    </CardContent>*/}
                                {/*</Card>*/}
                            </div>
                        )}
                        {activeForm === 'Personal Info' && (
                            <Card className="mx-auto">
                                <CardContent>
                                    <Form {...formUserData}>
                                        {// @ts-expect-error - fix this
                                            <form onSubmit={formUserData.handleSubmit(onSubmitUserData)}
                                                  className="space-y-4">
                                                <div className="grid gap-4 p-10">
                                                    <div className="grid gap-2">
                                                        <FormField
                                                            control={formUserData.control}
                                                            name="name"
                                                            render={({field}) => (
                                                                <FormItem>
                                                                    <FormLabel className="text-black">First
                                                                        Name</FormLabel>
                                                                    <FormControl>
                                                                        <Input placeholder={account?.name} {...field}/>
                                                                    </FormControl>
                                                                    <FormMessage/>
                                                                </FormItem>
                                                            )}
                                                        />
                                                    </div>
                                                    <div className="grid gap-2">
                                                        <FormField
                                                            control={formUserData.control}
                                                            name="lastName"
                                                            render={({field}) => (
                                                                <FormItem>
                                                                    <FormLabel className="text-black">Last
                                                                        Name</FormLabel>
                                                                    <FormControl>
                                                                        <Input
                                                                            placeholder={account?.lastname} {...field}/>
                                                                    </FormControl>
                                                                    <FormMessage/>
                                                                </FormItem>
                                                            )}
                                                        />
                                                    </div>
                                                    <div className="grid gap-2">
                                                        <FormField
                                                            control={formUserData.control}
                                                            name="phoneNumber"
                                                            render={({field}) => (
                                                                <FormItem className="items-start">
                                                                    <FormLabel className="text-black text-center">Phone
                                                                        Number</FormLabel>
                                                                    <FormControl className="w-full">
                                                                        <PhoneInput //TODO fix this
                                                                            placeholder={e164Number} {...field}/>
                                                                    </FormControl>
                                                                    <FormMessage/>
                                                                </FormItem>
                                                            )}
                                                        />
                                                    </div>
                                                    <Button type="submit" className="w-full pb-2">
                                                        Save changes
                                                    </Button>
                                                </div>
                                            </form>
                                        }
                                    </Form>
                                </CardContent>
                            </Card>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default AccountSettings;